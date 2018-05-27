package github.com.PERS23.MazeSolver;

import java.awt.*;

public class ControllerSettings {

    private int mMazeWidth;
    private int mMazeHeight;
    private int mCorridorSize;
    private int mWallSize;

    private Point mStartPoint;
    private Point mEndPoint;

    public ControllerSettings() {
        mMazeWidth = 20;
        mMazeHeight = 20;
        mCorridorSize = 24;
        mWallSize = 12;
        mStartPoint = new Point(0, 0);
        mEndPoint = new Point(19, 19);
    }

    public int getMazeWidth() {
        return mMazeWidth;
    }

    public void setMazeWidth(int mazeWidth) {
        mMazeWidth = mazeWidth;
    }

    public int getMazeHeight() {
        return mMazeHeight;
    }

    public void setMazeHeight(int mazeHeight) {
        mMazeHeight = mazeHeight;
    }

    public int getCorridorSize() {
        return mCorridorSize;
    }

    public void setCorridorSize(int corridorSize) {
        mCorridorSize = corridorSize;
    }

    public int getWallSize() {
        return mWallSize;
    }

    public void setWallSize(int wallSize) {
        mWallSize = wallSize;
    }

    public Point getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(int x, int y) {
        mStartPoint = new Point(x, y);
    }

    public Point getEndPoint() {
        return mEndPoint;
    }

    public void setEndPoint(int x, int y) {
        mEndPoint = new Point(x, y);
    }
}
