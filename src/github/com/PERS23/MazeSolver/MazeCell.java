package github.com.PERS23.MazeSolver;

public class MazeCell {

    private boolean[] mWalls; // NORTH, EAST, SOUTH, WEST

    public MazeCell() {
        mWalls = new boolean[] {true, true, true, true};
    }

    public boolean isWall(Direction choice) {
        return mWalls[choice.getWallIndex()];
    }

    public void carve(Direction choice) {
        mWalls[choice.getWallIndex()] = false;
    }
}
