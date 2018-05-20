package github.com.PERS23;

public enum Direction {
    NORTH(0, 0, -1),
    EAST(1, 1, 0),
    SOUTH(2, 0, 1),
    WEST(3, -1, 0);

    private int mWallIndex;
    private int x;
    private int y;

    private Direction(int wallIndex, int x, int y) {
        mWallIndex = wallIndex;
        this.x = x;
        this.y = y;
    }

    public int getWallIndex() {
        return mWallIndex;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direction opposite() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                return null;
        }
    }
}
