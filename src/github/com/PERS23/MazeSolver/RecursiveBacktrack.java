package github.com.PERS23.MazeSolver;

import java.util.Random;

public class RecursiveBacktrack implements GenerationStrategy {

    private Maze mCurrentMaze;
    private boolean[][] mVisited;
    private Random mRandomGenerator;

    @Override
    public Maze generateRandomMaze(int width, int height) {
        mRandomGenerator = new Random();
        mCurrentMaze = new Maze(width, height);
        mVisited = new boolean[height][width];

        backtrack(mRandomGenerator.nextInt(width), mRandomGenerator.nextInt(height));

        return mCurrentMaze;
    }

    /*
     *  Recursive backtracking:
     *  1. Choose a starting point in the field.
     *  2. Randomly choose a wall at that point and carve a passage through to the adjacent cell,
     *     but only if the adjacent cell has not been visited yet. This becomes the new current cell.
     *  3. If all adjacent cells have been visited, back up to the last cell that has uncarved walls and repeat.
     *  4. The algorithm ends when the process has backed all the way up to the starting point.
     */
    private void backtrack(int x, int y) {
        Direction[] choices = Direction.values();
        shuffleDirections(choices);

        for (Direction choice : choices) {
            if (!hasBeenVisited(x, y, choice)) {
                mCurrentMaze.carveWall(x, y, choice);
                mVisited[y][x] = true;
                backtrack(x + choice.getX(), y + choice.getY());
            }
        }
    }

    private void shuffleDirections(Direction[] choices) {
        for (int i = 0; i < choices.length; i++) {
            int j = mRandomGenerator.nextInt(choices.length - i);          // Choose rand pos in list up to n - i
            Direction tmp = choices[i];                                              // Swap i with that random position
            choices[i] = choices[j];
            choices[j] = tmp;
        }
    }

    private boolean hasBeenVisited(int x, int y, Direction choice) {
        if (mCurrentMaze.isWithinBounds(x + choice.getX(), y + choice.getY())) {
            return mVisited[y + choice.getY()][x + choice.getX()];
        }
        return true;
    }
}
