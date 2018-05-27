package github.com.PERS23.MazeSolver;

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
        Graph<Point, DefaultWeightedEdge> mazeGraph = placeVertices(target, start, end);
        linkVertices(mazeGraph, target);
        return Dijkstra(mazeGraph, start, end);

    }

    private Graph<Point, DefaultWeightedEdge> placeVertices(Maze target, Point start, Point end) {
        Graph<Point, DefaultWeightedEdge> mazeGraph = new DefaultUndirectedWeightedGraph<>(
        null, () -> new DefaultWeightedEdge());

        mazeGraph.addVertex(start);
        mazeGraph.addVertex(end);

        for (int y = 0; y < target.getHeight(); y++) {
            for (int x = 0; x < target.getWidth(); x++) {
                if (target.getNumOfWalls(x, y) == 3){
                    mazeGraph.addVertex(new Point(x, y));
                } else if (!target.isWall(x, y, Direction.NORTH) || !target.isWall(x, y, Direction.SOUTH)) {
                    if (!target.isWall(x, y, Direction.EAST) || !target.isWall(x, y, Direction.WEST)) {
                        mazeGraph.addVertex(new Point(x, y));
                    }
                }
            }
        }

        return mazeGraph;
    }

    private void linkVertices(Graph<Point, DefaultWeightedEdge> mazeGraph, Maze target) {
        List<Pair<Point, Point>> edgesToAdd = new LinkedList<>();
        List<Double> weights = new LinkedList<>();

        // If the graph is modified while an iteration over the set is in progress, the results of the iteration are undefined.
        for (Point current : mazeGraph.vertexSet()) {
            for (Direction choice : Direction.values()) {
                Point opposite = searchForLink(mazeGraph, current, target, choice);
                if (opposite != null) {
                    edgesToAdd.add(new Pair<>(current, opposite));
                    weights.add(current.distance(opposite));
                }
            }
        }

        for (int i = 0; i < edgesToAdd.size(); i++) {
            Point source = edgesToAdd.get(i).getKey();
            Point dest = edgesToAdd.get(i).getValue();
            // If the underlying graph implementation's Graph.getEdgeSupplier() returns null, then this method cannot create edges and throws an UnsupportedOperationException.
            mazeGraph.addEdge(source, dest);
            mazeGraph.setEdgeWeight(mazeGraph.getEdge(source, dest), weights.get(i));
        }
    }

    private Point searchForLink(Graph<Point, DefaultWeightedEdge> mazeGraph, Point vertex, Maze target,
                                Direction direction) {
        Point current = new Point(vertex.x, vertex.y);
        // While you're not facing a wall and the one you've got to is a vertex in the graph
        while (!target.isWall(current.x, current.y, direction)) {
            current.translate(direction.getDX(), direction.getDY());
            if (mazeGraph.containsVertex(current)) {
                return current;
            }
        }

        return null;
    }

    private Pair<List<Point>, List<Point>> Dijkstra(Graph<Point, DefaultWeightedEdge> mazeGraph, Point start, Point end) {
        Map<Point, Double> cloud = new HashMap<>();
        Map<Point, Point> prev = new HashMap<>();
        PriorityQueue<PQPointAdapter> pQueue = new PriorityQueue<>();
        List<Pair<Point, Point>> edgesTaken = new LinkedList<>();

        initDijkstra(mazeGraph, cloud, pQueue, start);

        while (!pQueue.isEmpty()) {
            PQPointAdapter currentPQElement = pQueue.poll();
            Point currentVertex = currentPQElement.getVertex();

            for (Point adjacentVertex : Graphs.neighborListOf(mazeGraph, currentVertex)) {
                edgesTaken.add(new Pair<>(currentVertex, adjacentVertex));
                PQPointAdapter adjacentPQElement = new PQPointAdapter(adjacentVertex, cloud.get(adjacentVertex));

                double relaxWeight = cloud.get(currentVertex) +
                                     mazeGraph.getEdgeWeight(mazeGraph.getEdge(currentVertex, adjacentVertex));

                if (adjacentPQElement.getShortestDist() > relaxWeight) {
                    pQueue.remove(adjacentPQElement);
                    adjacentPQElement.setShortestDist(relaxWeight);
                    pQueue.offer(adjacentPQElement);

                    cloud.put(adjacentVertex, relaxWeight);
                    prev.put(adjacentVertex, currentVertex);
                }
            }
        }

        System.out.println(cloud.get(end));
        return new Pair<>(getPointsExplored(edgesTaken), getSolutionPoints(prev, start, end));
    }

    private void initDijkstra(Graph<Point, DefaultWeightedEdge> mazeGraph, Map<Point, Double> cloud, PriorityQueue<PQPointAdapter> pQueue, Point start) {
        cloud.put(start, 0.0);
        pQueue.offer(new PQPointAdapter(start, 0.0));

        for(Point p : mazeGraph.vertexSet()) {
            if (!p.equals(start)) {
                PQPointAdapter current = new PQPointAdapter(p);
                cloud.put(p, current.getShortestDist());
                pQueue.offer(current);
            }
        }
    }

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
        solutionPoints.add(end);

        return solutionPoints;
    }

    private List<Point> getPointsExplored(List<Pair<Point, Point>> edges) {
        List<Point> points = new LinkedList<>();

        for (Pair<Point, Point> edge : edges) {
            points.addAll(getPointsFromEdge(edge));
        }

        return points;
    }

    // DDA Algorithm: https://www.tutorialspoint.com/computer_graphics/line_generation_algorithm.htm
    private List<Point> getPointsFromEdge(Pair<Point, Point> edge) {
        List<Point> inbetweeners = new LinkedList<>();
        Point source = edge.getKey();
        Point dest = edge.getValue();

        int dx = dest.x - source.x;
        int dy = dest.y - source.y;
        int steps;

        if (Math.abs(dx) > Math.abs(dy)) {
            steps = Math.abs(dx);
        } else {
            steps = Math.abs(dy);
        }

        int x = source.x;
        double xIncrement = dx / (double) steps;
        int y = source.y;
        double yIncrement = dy / (double) steps;

        for (int i = 0; i < steps; i++) {
            x += xIncrement;
            y += yIncrement;
            inbetweeners.add(new Point(x, y));
        }

        return inbetweeners;
    }
}
