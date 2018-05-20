package github.com.PERS23;

import java.util.Arrays;
import java.util.Random;

public class Maze {

    private MazeCell[][] mLabyrinth; // [row][col], [y][x]
    private boolean[][] mVisitedTracker;
    private Random mDirectionRandomiser;

    public static Maze randomMaze(int width, int height) {
        Maze randomMaze = new Maze(width, height);
        Random randomGen = new Random();

        randomMaze.recursiveBacktrack(randomGen.nextInt(width), randomGen.nextInt(height));

        return randomMaze;
    }

    public Maze(int width, int height) {
        mLabyrinth = new MazeCell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mLabyrinth[y][x] = new MazeCell();
            }
        }
        mVisitedTracker = new boolean[height][width];
        mDirectionRandomiser = new Random();
    }

    /* Recursive backtracking:
     *  1. Choose a starting point in the field.
     *  2. Randomly choose a wall at that point and carve a passage through to the adjacent cell,
     *     but only if the adjacent cell has not been visited yet. This becomes the new current cell.
     *  3. If all adjacent cells have been visited, back up to the last cell that has uncarved walls and repeat.
     *  4. The algorithm ends when the process has backed all the way up to the starting point.
     */
    private void recursiveBacktrack(int x, int y) {
        Direction[] choices = Direction.values();
        shuffleDirections(choices);

        for (Direction choice : choices) {
            if (!hasBeenVisited(x, y, choice)) {
                carveWall(x, y, choice);
                mVisitedTracker[y][x] = true;
                recursiveBacktrack(x + choice.getX(), y + choice.getY());
            }
        }
    }

    private void carveWall(int x, int y, Direction choice) {
        if (isWithinBounds(x, y) && isWithinBounds(x + choice.getX(), y + choice.getY())) {
            mLabyrinth[y][x].carve(choice);
            mLabyrinth[y + choice.getY()][x + choice.getX()].carve(choice.opposite());
        }
    }

    private boolean hasBeenVisited(int x, int y, Direction choice) {
        if (isWithinBounds(x, y) && isWithinBounds(x + choice.getX(), y + choice.getY())) {
            return mVisitedTracker[y + choice.getY()][x + choice.getX()];
        }
        return true;
    }

    /* Simple Fisher–Yates shuffle
     */
    private void shuffleDirections(Direction[] choices) {
        for (int i = 0; i < choices.length; i++) {
            int j = mDirectionRandomiser.nextInt(choices.length - i);    // Choose random pos in list up to n - i
            Direction tmp = choices[i];                                              // Swap i with that random position
            choices[i] = choices[j];
            choices[j] = tmp;
        }
    }

    public int getWidth() {
        return mLabyrinth[0].length;
    }

    public int getHeight() {
        return mLabyrinth.length;
    }

    public boolean isWithinBounds(int x, int y) {
        return (x >= 0 && x < getWidth()) && (y >= 0 && y < getHeight());
    }
}
