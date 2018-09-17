package PERS23.MazeSolver;

import java.awt.*;
import java.util.Objects;

// Wrapper class for vertices, for use in the PQ for Dijkstra.
public class PQPointAdapter implements Comparable<PQPointAdapter> {

    private Point mVertex;
    private double mShortestDist;

    public PQPointAdapter(Point vertex) {
        this(vertex, Double.MAX_VALUE);
    }

    public PQPointAdapter(Point vertex, double shortestDist) {
        mVertex = vertex;
        mShortestDist = shortestDist;
    }

    public Point getVertex() {
        return mVertex;
    }

    public double getShortestDist() {
        return mShortestDist;
    }

    public void setShortestDist(double shortestDist) {
        mShortestDist = shortestDist;
    }

    @Override
    public int compareTo(PQPointAdapter other) {
        if (this.mShortestDist < other.mShortestDist) {
            return -1;
        } else if (this.mShortestDist == other.mShortestDist) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        PQPointAdapter that = (PQPointAdapter) other;
        return Double.compare(that.mShortestDist, mShortestDist) == 0 && Objects.equals(mVertex, that.mVertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mVertex);
    }
}
