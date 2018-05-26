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
        Graph<Point, DefaultWeightedEdge> mazeGraph = new DefaultUndirectedWeightedGraph<Point, DefaultWeightedEdge>(
        null, () -> new DefaultWeightedEdge());

        mazeGraph.addVertex(start);
        mazeGraph.addVertex(end);

        for (int y = 0; y < target.getHeight(); y++) {
            for (int x = 0; x < target.getWidth(); x++) {
                if (target.getNumOfAdjWalls(x, y) == 3){
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
        PriorityQueue<PQPointAdapter> pQueue = new PriorityQueue<>();

        initDijkstra(mazeGraph, cloud, pQueue, start);

        while (!pQueue.isEmpty()) {
            PQPointAdapter queueElement = pQueue.poll();
            Point current = queueElement.getVertex();

            for (Point adjacent : Graphs.neighborListOf(mazeGraph, current)) {
                double relaxWeight = cloud.get(current) + mazeGraph.getEdgeWeight(mazeGraph.getEdge(current, adjacent));
                if (cloud.get(adjacent) > relaxWeight) {
                    queueElement.setShortestDist(relaxWeight);
                    pQueue.offer(queueElement);
                    cloud.put(adjacent, relaxWeight);
                }
            }
        }

        System.out.println(cloud.get(end));

        return new Pair<>(new ArrayList<>(mazeGraph.vertexSet()), new ArrayList<>());
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

    private List<Point> getPointsFromEdge(Point v1, Point v2) {


        return null;
    }
}
