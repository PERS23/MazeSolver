package github.com.PERS23.Graph;

import java.util.ArrayList;
import java.util.List;

// Upper bounded by ALVertex and ALEdge so interfaces are compatible.
public class ALGraph<V extends ALVertex, E extends ALEdge> implements GraphADT<V, E> {

    private List<ALVertex> mVertices;
    private List<ALEdge> mEdges;

    public List<V> vertices() {
        return mVertices;
    }

    public List<ALEdge> edges() {
        return mEdges;
    }

    @Override
    public boolean areAdjacent(V x, V y) {
        return x.checkIfAdjacent(y);
    }

    public List<ALVertex> neighboursOf(V x) {
        List<ALVertex> neighbours = new ArrayList<>();
        for (ALEdge e : incidentEdges(x)) {
            neighbours.add(e.getOpposite(x));
        }
        return neighbours;
    }

    @Override
    public List<E> incidentEdges(V x) {
        return new ArrayList<E>(x.getIncidentEdges());
    }

    @Override
    public void addVertex(V x) {
        mVertices.add(x);
    }

    @Override
    public void removeVertex(V x) {
        mVertices.remove(x);
        x.clearEdges();
    }

    @Override
    public void addEdge(V x, V y) {
        mEdges.add(new ALEdge(x, y));
    }

    @Override
    public void removeEdge(V x, V y) {
        ALEdge connection = null;
        for (ALEdge e : incidentEdges(x)) {
            if (e.getOpposite(x).equals(y)) {
                connection = e;
            }
        }
        mEdges.remove(connection);
        connection.unhook();
    }

    @Override
    public void setEdgeWeight(E e, double w) {
        e.setWeight(w);
    }
}
