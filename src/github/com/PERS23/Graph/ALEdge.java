package github.com.PERS23.Graph;

import java.util.Objects;

public class ALEdge {

    private ALVertex mEndPointA;
    private ALVertex mEndPointB;
    private Double mWeight;

    public ALEdge(ALVertex endPointA, ALVertex endPointB) {
        this(endPointA, endPointB, Double.MAX_VALUE);
    }

    public ALEdge(ALVertex endPointA, ALVertex endPointB, Double weight) {
        mEndPointA = endPointA;
        mEndPointB = endPointB;
        mWeight = weight;
        mEndPointA.hookEdge(this);
        mEndPointB.hookEdge(this);
    }

    public Double getWeight() {
        return mWeight;
    }

    public void setWeight(Double weight) {
        mWeight = weight;
    }

    public void unhook() {
        mEndPointA.unhookEdge(this);
        mEndPointA = null;

        mEndPointB.unhookEdge(this);
        mEndPointB = null;
    }

    public ALVertex getOpposite(ALVertex x) {
        if (x.equals(mEndPointA)) {
            return mEndPointB;
        } else if (x.equals(mEndPointB)) {
            return mEndPointA;
        } else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ALEdge other = (ALEdge) o;
        return this.mEndPointA == other.mEndPointA &&
               this.mEndPointB == other.mEndPointB &&
               this.mWeight == other.mWeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mEndPointA, mEndPointB);
    }
}
