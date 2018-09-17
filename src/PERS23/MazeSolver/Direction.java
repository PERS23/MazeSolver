package PERS23.MazeSolver;

public enum Direction {
    NORTH(0, 0, -1),
    EAST(1, 1, 0),
    SOUTH(2, 0, 1),
    WEST(3, -1, 0);

    private int mWallIndex;
    private int dx;
    private int dy;

    private Direction(int wallIndex, int dx, int dy) {
        mWallIndex = wallIndex;
        this.dx = dx;
        this.dy = dy;
    }

    public int getWallIndex() {
        return mWallIndex;
    }

    public int getDX() {
        return dx;
    }

    public int getDY() {
        return dy;
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
