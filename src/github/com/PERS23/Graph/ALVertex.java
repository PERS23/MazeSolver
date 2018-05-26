package github.com.PERS23.Graph;

import java.util.*;

// Data held by this should be immutable, so to update it first clone the data object and use the set method provided.
public class ALVertex<T> {

    private T mData;
    private Set<ALEdge> mIncidentEdges;

    public ALVertex(T data) {
        mData = data;
        mIncidentEdges = new HashSet<>();
    }

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public boolean checkIfAdjacent(ALVertex x) {
        if (x == this) {
            return true;
        }

        for (ALEdge e : mIncidentEdges) {
            if (x.equals(e.getOpposite(this))) {
                return true;
            }
        }

        return false;
    }

    public Set<ALEdge> getIncidentEdges() {
        return mIncidentEdges;
    }

    public void hookEdge(ALEdge e) { // Wow that's extremely Edge e
        mIncidentEdges.add(e);
    }

    public void unhookEdge(ALEdge e) {
        mIncidentEdges.remove(e);
    }

    public void clearEdges() {
        for (ALEdge e : mIncidentEdges) {
            e.unhook();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ALVertex<?> other = (ALVertex<?>) o;
        return Objects.equals(this.mData, other.mData) &&
               Objects.equals(this.mIncidentEdges, other.mIncidentEdges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mData);
    }
}
