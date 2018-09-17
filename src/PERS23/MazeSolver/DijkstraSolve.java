package PERS23.MazeSolver;

import javafx.util.Pair;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DijkstraSolve implements SolvingStrategy {

    @Override
    public Pair<List<Point>, List<Point>> solve(Maze target, Point start, Point end) {
        Graph<Point, DefaultWeightedEdge> mazeGraph = createVertices(target, start, end);
        createEdges(mazeGraph, target);
        return Dijkstra(mazeGraph, start, end);

    }

    private Graph<Point, DefaultWeightedEdge> createVertices(Maze target, Point start, Point end) {
        Graph<Point, DefaultWeightedEdge> mazeGraph = new DefaultUndirectedWeightedGraph<>(
        null, () -> new DefaultWeightedEdge());          // Need edge supplier otherwise we can't add edges
                                   // Explicitly add the start and end otherwise they might not get picked up in process
        mazeGraph.addVertex(start);
        mazeGraph.addVertex(end);
                                                                                          // For every point in the maze
        for (int y = 0; y < target.getHeight(); y++) {
            for (int x = 0; x < target.getWidth(); x++) {
                      // First check if the point is a dead-end, then a junction. If either are true it becomes a vertex
                if (target.getNumOfWalls(x, y) == 3) {
                    mazeGraph.addVertex(new Point(x, y));
                } else if (!target.isWall(x, y, Direction.NORTH) || !target.isWall(x, y, Direction.SOUTH)) {
                    if (!target.isWall(x, y, Direction.EAST) || !target.isWall(x, y, Direction.WEST)) {
                        mazeGraph.addVertex(new Point(x, y));
                    }
                }
            }
        }

        return mazeGraph;                                                           // Edges get assigned in createEdges
    }

    /* Very inefficient, could probably improve run time by introducing direction flags for nodes. When a edge is
     * created a flag in the opposite node is created saying it has already been linked in that direction. This would
     * reduce any unnecessary work.
     */
    private void createEdges(Graph<Point, DefaultWeightedEdge> mazeGraph, Maze target) {
        List<Pair<Point, Point>> edgesToAdd = new LinkedList<>();
        List<Double> weights = new LinkedList<>();
            // Graph uses fast fail iterators so have to save to be edges out to a list explicitly and then add them all
        for (Point vertex : mazeGraph.vertexSet()) {                                    // For every vertex in the graph
                                                       // Search for another vertex to link it to in all NESW directions
            for (Direction choice : Direction.values()) {
                Point opposite = searchForLink(mazeGraph, vertex, target, choice);
                if (opposite != null) {
                    edgesToAdd.add(new Pair<>(vertex, opposite));
                    weights.add(vertex.distance(opposite));                      // Explicitly saving weight out as well
                }
            }
        }
                                                                                // Actual edge creation and adding stage
        for (int i = 0; i < edgesToAdd.size(); i++) {
            Point source = edgesToAdd.get(i).getKey();
            Point dest = edgesToAdd.get(i).getValue();
            mazeGraph.addEdge(source, dest);
            mazeGraph.setEdgeWeight(mazeGraph.getEdge(source, dest), weights.get(i));
        }
    }

    private Point searchForLink(Graph<Point, DefaultWeightedEdge> mazeGraph, Point vertex, Maze target,
                                Direction direction) {
        Point current = new Point(vertex.x, vertex.y);
                                                                                       // While you're not facing a wall
        while (!target.isWall(current.x, current.y, direction)) {
            current.translate(direction.getDX(), direction.getDY());
            if (mazeGraph.containsVertex(current)) {      // If the one you got to is a vertex, stop as you found a link
                return current;
            }
        }

        return null;
    }

    private Pair<List<Point>, List<Point>> Dijkstra(Graph<Point, DefaultWeightedEdge> mazeGraph, Point start, Point end) {
        Map<Point, Double> cloud = new HashMap<>();                            // Cloud used for Edge Relaxation process
        Map<Point, Point> prev = new HashMap<>();                                  // Backtrack map for finding solution
        PriorityQueue<PQPointAdapter> pQueue = new PriorityQueue<>();
        List<Pair<Point, Point>> edgesTaken = new LinkedList<>();

        initDijkstra(mazeGraph, cloud, pQueue, start);

        while (!pQueue.isEmpty()) {
            PQPointAdapter currentPQElement = pQueue.poll();                        // Get the min element out of the PQ
            Point currentVertex = currentPQElement.getVertex();     // Get vertex out of element, so can lookup in graph
                                                                         // For all the neighbours of the current vertex
            for (Point adjacentVertex : Graphs.neighborListOf(mazeGraph, currentVertex)) {
                PQPointAdapter adjacentPQElement = new PQPointAdapter(adjacentVertex, cloud.get(adjacentVertex));

                double relaxWeight = cloud.get(currentVertex) +
                                     mazeGraph.getEdgeWeight(mazeGraph.getEdge(currentVertex, adjacentVertex));

                if (adjacentPQElement.getShortestDist() > relaxWeight) {
                    pQueue.remove(adjacentPQElement);                  // Update in PQ requires removal and re-insertion
                    adjacentPQElement.setShortestDist(relaxWeight);
                    pQueue.offer(adjacentPQElement);
                                                                           // Record the route you went down for display
                    edgesTaken.add(new Pair<>(currentVertex, adjacentVertex));
                                                       // Update the cloud and backtrack map if you found a shorter path
                    cloud.put(adjacentVertex, relaxWeight);
                    prev.put(adjacentVertex, currentVertex);
                }
            }
        }

        List<Point> allTaken = getPointsExplored(edgesTaken);
        allTaken.add(0, start);
        return new Pair<>(allTaken, getSolutionPoints(prev, start, end));
    }

    private void initDijkstra(Graph<Point, DefaultWeightedEdge> mazeGraph, Map<Point, Double> cloud, PriorityQueue<PQPointAdapter> pQueue, Point start) {
        cloud.put(start, 0.0);                                                  // Put the start as having a weight of 0
        pQueue.offer(new PQPointAdapter(start, 0.0));
                        // Create all the other vertices, constructor w/o weight specification auto defaults to infinity
        for(Point p : mazeGraph.vertexSet()) {
            if (!p.equals(start)) {                                               // Don't add the starting vertex again
                PQPointAdapter current = new PQPointAdapter(p);
                cloud.put(p, current.getShortestDist());
                pQueue.offer(current);
            }
        }
    }

    /* Cycles through the backtrack map to reconstruct the solution route.
     */
    private List<Point> getSolutionPoints(Map<Point, Point> backtrack, Point start, Point end) {
        List<Point> solutionPoints = new LinkedList<>();
        List<Pair<Point, Point>> solutionEdges = new LinkedList<>();

        Point prev = backtrack.get(end);
        Point current = end;

        while (backtrack.containsKey(current)) {
            solutionEdges.add(0, new Pair<>(current, prev));

            current = prev;
            prev = backtrack.get(prev);
        }

        for (Pair<Point, Point> edge : solutionEdges) {
            solutionPoints.addAll(getPointsFromEdge(edge));
        }
        solutionPoints.add(end);                     // Explicitly add end and line algorithm doesn't include this point

        return solutionPoints;
    }

    /* Walks through the edges taken list in order to reconstruct the general route.
     */
    private List<Point> getPointsExplored(List<Pair<Point, Point>> edges) {
        List<Point> points = new LinkedList<>();

        for (Pair<Point, Point> edge : edges) {
            points.addAll(getPointsFromEdge(edge));
        }

        return points;
    }

    /* Responsible for returning the list of x/y points that make up an edge (so animation builder can place them).
     * DDA Algorithm: https://www.tutorialspoint.com/computer_graphics/line_generation_algorithm.htm
     */
    private List<Point> getPointsFromEdge(Pair<Point, Point> edge) {
        List<Point> inbetweeners = new LinkedList<>();
        Point source = edge.getKey();
        Point dest = edge.getValue();
                                                                                     // Calc diff between two end points
        int dx = dest.x - source.x;
        int dy = dest.y - source.y;

        int steps;                                                // Identify the num of steps needed to create the line
        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }
                                                                     // Calculate the increment to apply to both x and y
        int x = source.x;
        double xIncrement = dx / (double) steps;
        int y = source.y;
        double yIncrement = dy / (double) steps;
                                                   // Simply increment the start point "steps" times and record each one
        for (int i = 0; i < steps; i++) {
            x += xIncrement;
            y += yIncrement;
            inbetweeners.add(new Point(x, y));
        }

        return inbetweeners;
    }
}
