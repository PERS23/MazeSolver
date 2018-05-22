package github.com.PERS23.MazeSolver;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class AnimationBuilder {

    private final Paint mSolveColor = Paint.valueOf("#0000FF");
    private final Paint mHighlightColor = Paint.valueOf("#FF0000");
    private final Paint mCorridorColor = Paint.valueOf("#FFFFFF");
    private final Paint mWallColor = Paint.valueOf("#000000");

    private final int mWallThickness = 4;
    private final int mCorridorThickness = 8;

    private Maze mSource;
    private boolean mHighlighted[][];
    private Canvas mCanvas;
    private GraphicsContext mGraphicsContext;

    public AnimationBuilder(Maze source) {
        mSource = source;
        mHighlighted = new boolean[source.getHeight()][source.getWidth()];
                                                                                      // 1 extra wall to take account of
        int width = mWallThickness + source.getWidth() * (mCorridorThickness + mWallThickness);
        int height = mWallThickness + source.getHeight() * (mCorridorThickness + mWallThickness);

        mCanvas = new Canvas(width, height);
        mGraphicsContext = mCanvas.getGraphicsContext2D();

        mGraphicsContext.setFill(mWallColor);
             // Make the image all wall to start off with, then perform unhighlight on all squares to carve the maze out
        mGraphicsContext.fillRect(0, 0, width, height);
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                unhighlight(x, y);
            }
        }
    }

    public void highlight(int x, int y) {
        mHighlighted[y][x] = true;
        fillCell(x, y, mHighlightColor);
    }

    public void unhighlight(int x, int y) {
        mHighlighted[y][x] = false;
        fillCell(x, y, mCorridorColor);
    }

    private void fillCell(int x, int y, Paint color) {
                                                             // Grab the upper left hand corner of the cell in the image
        int imageX = mWallThickness + x * (mCorridorThickness + mWallThickness);
        int imageY = mWallThickness + y * (mCorridorThickness + mWallThickness);

        mGraphicsContext.setFill(color);
                                                                                   // Fill the cell with the given color
        mGraphicsContext.fillRect(imageX, imageY, mCorridorThickness, mCorridorThickness);

        if (!mSource.isWall(x, y, Direction.NORTH) &&        // Only if the neighbour is the same highlight value as you
            mHighlighted[y][x] == mHighlighted[y + Direction.NORTH.getDY()][x + Direction.NORTH.getDX()]) {
                                                                                             // Up by the wall thickness
            mGraphicsContext.fillRect(imageX, imageY - mWallThickness, mCorridorThickness, mWallThickness);
        }

        if (!mSource.isWall(x, y, Direction.EAST) && // Always a wall at the bound, so the 2nd check won't run with err
            mHighlighted[y][x] == mHighlighted[y + Direction.EAST.getDY()][x + Direction.EAST.getDX()]) {
                                                                                      // Right by the corridor thickness
            mGraphicsContext.fillRect(imageX + mCorridorThickness, imageY, mWallThickness, mCorridorThickness);
        }

        if (!mSource.isWall(x, y, Direction.SOUTH) &&
            mHighlighted[y][x] == mHighlighted[y + Direction.SOUTH.getDY()][x + Direction.SOUTH.getDX()]) {
                                                                                       // Down by the corridor thickness
            mGraphicsContext.fillRect(imageX, imageY + mCorridorThickness, mCorridorThickness, mWallThickness);
        }

        if (!mSource.isWall(x, y, Direction.WEST) &&
            mHighlighted[y][x] == mHighlighted[y + Direction.WEST.getDY()][x + Direction.WEST.getDX()]) {
                                                                                           // Left by the wall thickness
            mGraphicsContext.fillRect(imageX - mWallThickness, imageY, mWallThickness, mCorridorThickness);
        }
    }

    public Image getImage() {
        return mCanvas.snapshot(null, null);
    }
}
