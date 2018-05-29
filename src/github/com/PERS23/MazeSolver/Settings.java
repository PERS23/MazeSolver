package github.com.PERS23.MazeSolver;

import javafx.beans.property.SimpleIntegerProperty;

import java.awt.*;

public class Settings {

    private SimpleIntegerProperty mMazeWidth;
    private SimpleIntegerProperty mMazeHeight;
    private SimpleIntegerProperty mCorridorSize;
    private SimpleIntegerProperty mWallSize;
    private SimpleIntegerProperty mStartX;
    private SimpleIntegerProperty mStartY;
    private SimpleIntegerProperty mEndX;
    private SimpleIntegerProperty mEndY;

    public Settings() {
        mMazeWidth = new SimpleIntegerProperty(20);
        mMazeHeight = new SimpleIntegerProperty(20);
        mCorridorSize = new SimpleIntegerProperty(14);
        mWallSize = new SimpleIntegerProperty(12);
        mStartX = new SimpleIntegerProperty(0);
        mStartY = new SimpleIntegerProperty(0);
        mEndX = new SimpleIntegerProperty(19);
        mEndY = new SimpleIntegerProperty(19);
    }

    public int getMazeWidth() {
        return mMazeWidth.get();
    }

    public SimpleIntegerProperty mazeWidthProperty() {
        return mMazeWidth;
    }

    public void setMazeWidth(int mazeWidth) {
        this.mMazeWidth.set(mazeWidth);
    }

    public int getMazeHeight() {
        return mMazeHeight.get();
    }

    public SimpleIntegerProperty mazeHeightProperty() {
        return mMazeHeight;
    }

    public void setMazeHeight(int mazeHeight) {
        this.mMazeHeight.set(mazeHeight);
    }

    public int getCorridorSize() {
        return mCorridorSize.get();
    }

    public SimpleIntegerProperty corridorSizeProperty() {
        return mCorridorSize;
    }

    public void setCorridorSize(int corridorSize) {
        this.mCorridorSize.set(corridorSize);
    }

    public int getWallSize() {
        return mWallSize.get();
    }

    public SimpleIntegerProperty wallSizeProperty() {
        return mWallSize;
    }

    public void setWallSize(int wallSize) {
        this.mWallSize.set(wallSize);
    }

    public int getStartX() {
        return mStartX.get();
    }

    public SimpleIntegerProperty startXProperty() {
        return mStartX;
    }

    public void setStartX(int startX) {
        this.mStartX.set(startX);
    }

    public int getStartY() {
        return mStartY.get();
    }

    public SimpleIntegerProperty startYProperty() {
        return mStartY;
    }

    public void setStartY(int startY) {
        this.mStartY.set(startY);
    }

    public Point getStartPoint() {
        return new Point(mStartX.get(), mStartY.get());
    }

    public int getEndX() {
        return mEndX.get();
    }

    public SimpleIntegerProperty endXProperty() {
        return mEndX;
    }

    public void setEndX(int endX) {
        this.mEndX.set(endX);
    }

    public int getEndY() {
        return mEndY.get();
    }

    public SimpleIntegerProperty endYProperty() {
        return mEndY;
    }

    public void setEndY(int endY) {
        this.mEndY.set(endY);
    }

    public Point getEndPoint() {
        return new Point(mEndX.get(), mEndY.get());
    }
}
