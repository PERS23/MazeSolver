package github.com.PERS23.Graph;

import java.util.List;

/**
 * Abstract data type taken from: https://en.wikipedia.org/wiki/Graph_(abstract_data_type)
 * Edges and Vertices have to be constructed by user.
 * @param <V> Type associated with vertices.
 * @param <E> Type associated with edges.
 */
public interface GraphADT<V, E> {

    /**
     * Tests whether there is an edge from the vertex x to the vertex y.
     * @param x A vertex in the graph.
     * @param y A vertex in the graph.
     * @return True if x and y are adjacent and false otherwise.
     */
    boolean areAdjacent(V x, V y);

    /**
     * Finds and returns the list of edges that are incident to a given vertex.
     * @param x A vertex in the graph.
     * @return A list of edges.
     */
    List<E> incidentEdges(V x);

    /**
     * Adds the vertex x, if it is not there.
     * @param x A vertex in the graph.
     */
    void addVertex(V x);

    /**
     * Removes the vertex x, if it is there.
     * @param x A vertex in the graph.
     */
    void removeVertex(V x);

    /**
     * Adds the edge from the vertex x to the vertex y, if it is not there.
     * @param x A vertex in the graph.
     * @param y A vertex in the graph.
     */
    void addEdge(V x, V y);

    /**
     * Removes the edge from the vertex x to the vertex y, if it is there.
     * @param x A vertex in the graph.
     * @param y A vertex in the graph.
     */
    void removeEdge(V x, V y);

    /**
     * Assigns weight w to an edge e.
     * @param e An edge in the graph.
     * @param w
     */
    void setEdgeWeight(E e, double w);
}
