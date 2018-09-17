package PERS23.MazeSolver;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;

public class MazeImageBuilder {

    private final Paint mSolveColor = Paint.valueOf("#0000FF");
    private final Paint mHighlightColor = Paint.valueOf("#FF0000");
    private final Paint mCorridorColor = Paint.valueOf("#FFFFFF");
    private final Paint mWallColor = Paint.valueOf("#000000");

    private final int mWallSize;
    private final int mCorridorSize;

    private Maze mSource;
    private boolean mNormalHighlights[][];
    private boolean mSolutionHighlights[][];
    private Canvas mCanvas;
    private GraphicsContext mGraphicsContext;

    public MazeImageBuilder(Maze source, int wallSize, int corridorSize) {
        mSource = source;
        mNormalHighlights = new boolean[source.getHeight()][source.getWidth()];
        mSolutionHighlights = new boolean[source.getHeight()][source.getWidth()];

        mWallSize = wallSize;
        mCorridorSize = corridorSize;
                                                                                      // 1 extra wall to take account of
        int width = mWallSize + source.getWidth() * (mCorridorSize + mWallSize);
        int height = mWallSize + source.getHeight() * (mCorridorSize + mWallSize);

        mCanvas = new Canvas(width, height);
        mGraphicsContext = mCanvas.getGraphicsContext2D();
        mGraphicsContext.setFill(mWallColor);
             // Make the image all wall to start off with, then perform unhighlight on all squares to carve the maze out
        mGraphicsContext.fillRect(0, 0, width, height);
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                unhighlightNormalPoint(x, y);
            }
        }
    }

    public void highlightNormalPoint(int x, int y) {
        mNormalHighlights[y][x] = true;
        fillCell(x, y, mHighlightColor, mNormalHighlights);
    }

    public void unhighlightNormalPoint(int x, int y) {
        mNormalHighlights[y][x] = false;
        fillCell(x, y, mCorridorColor, mNormalHighlights);
    }

    public void highlightSolutionPoint(int x, int y) {
        mSolutionHighlights[y][x] = true;
        fillCell(x, y, mSolveColor, mSolutionHighlights);
    }

    public void unhighlightSolutionPoint(int x, int y) {
        mSolutionHighlights[y][x] = false;
        fillCell(x, y, mHighlightColor, mNormalHighlights);
    }
                                      // Highlights parameter is used to determine how to fill the surrounding wall gaps
    private void fillCell(int x, int y, Paint color, final boolean[][] highlights) {
                                                             // Grab the upper left hand corner of the cell in the image
        int imageX = mWallSize + x * (mCorridorSize + mWallSize);
        int imageY = mWallSize + y * (mCorridorSize + mWallSize);

        mGraphicsContext.setFill(color);
                                                                                   // Fill the cell with the given color
        mGraphicsContext.fillRect(imageX, imageY, mCorridorSize, mCorridorSize);

        if (!mSource.isWall(x, y, Direction.NORTH) &&        // Only if the neighbour is the same highlight value as you
            highlights[y][x] == highlights[y + Direction.NORTH.getDY()][x + Direction.NORTH.getDX()]) {
                                                                                             // Up by the wall thickness
            mGraphicsContext.fillRect(imageX, imageY - mWallSize, mCorridorSize, mWallSize);
        }

        if (!mSource.isWall(x, y, Direction.EAST) &&  // Always a wall at the bound, so the 2nd check won't run with err
            highlights[y][x] == highlights[y + Direction.EAST.getDY()][x + Direction.EAST.getDX()]) {
                                                                                      // Right by the corridor thickness
            mGraphicsContext.fillRect(imageX + mCorridorSize, imageY, mWallSize, mCorridorSize);
        }

        if (!mSource.isWall(x, y, Direction.SOUTH) &&
            highlights[y][x] == highlights[y + Direction.SOUTH.getDY()][x + Direction.SOUTH.getDX()]) {
                                                                                       // Down by the corridor thickness
            mGraphicsContext.fillRect(imageX, imageY + mCorridorSize, mCorridorSize, mWallSize);
        }

        if (!mSource.isWall(x, y, Direction.WEST) &&
            highlights[y][x] == highlights[y + Direction.WEST.getDY()][x + Direction.WEST.getDX()]) {
                                                                                           // Left by the wall thickness
            mGraphicsContext.fillRect(imageX - mWallSize, imageY, mWallSize, mCorridorSize);
        }
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public Image getImage() {
        return mCanvas.snapshot(null, null);
    }

    public void reset() {
        for (int y = 0; y < mSource.getHeight(); y++) {
            for (int x = 0; x < mSource.getWidth(); x++) {
                mNormalHighlights[y][x] = false;
                mSolutionHighlights[y][x] = false;
                unhighlightNormalPoint(x, y);
            }
        }
    }
}
