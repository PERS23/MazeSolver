package github.com.PERS23.MazeSolver;

import java.awt.Point;
import java.util.*;

public class RecursiveBacktrack implements GenerationStrategy {

    private Maze mCurrentMaze;
    private boolean[][] mVisited;
    private int mVisitsLeft;
    private Random mRandomGenerator;

    @Override
    public Maze generateRandomMaze(int width, int height) {
        mRandomGenerator = new Random();
        mCurrentMaze = new Maze(width, height);
        mVisited = new boolean[height][width];
        mVisitsLeft = width * height;

        backtrack();

        return mCurrentMaze;
    }

    /* Recursive backtracking:
     *     1. Make the initial cell the current cell and mark it as visited
     *     2. While there are unvisited cells
     *         1. If the current cell has any neighbours which have not been visited
     *             1. Choose randomly one of the unvisited neighbours
     *             2. Push the current cell to the stack
     *             3. Remove the wall between the current cell and the chosen cell
     *             4. Make the chosen cell the current cell and mark it as visited
     *         2. Else if stack is not empty
     *             1. Pop a cell from the stack
     *             2. Make it the current cell
     */
    private void backtrack() {
        Deque<Point> cellStack = new ArrayDeque<>();

        Point currentCell = new Point(0, 0);
        visit(currentCell.x, currentCell.y);

        while (areUnvisitedCellsLeft()) {
            List<Direction> unvisitedDirections = getUnvisitedDirections(currentCell.x, currentCell.y);

            if (!unvisitedDirections.isEmpty()) {
                Direction choice = chooseRandomDirection(unvisitedDirections);
                cellStack.push(currentCell);
                mCurrentMaze.carveWall(currentCell.x, currentCell.y, choice);
                currentCell = new Point(currentCell.x + choice.getDX(), currentCell.y + choice.getDY());
                visit(currentCell.x, currentCell.y);
            } else if (!cellStack.isEmpty()) {
                currentCell = cellStack.pop();
            }
        }
    }

    private Direction chooseRandomDirection(List<Direction> unvisitedNeighbours) {
        return unvisitedNeighbours.get(mRandomGenerator.nextInt(unvisitedNeighbours.size()));
    }

    private List<Direction> getUnvisitedDirections(int x, int y) {
        List<Direction> neighbours = new ArrayList<>();

        for (Direction choice : Direction.values()) {
            if (mCurrentMaze.isWithinBounds(x + choice.getDX(), y + choice.getDY()) &&
                !mVisited[y + choice.getDY()][x + choice.getDX()]) {
                neighbours.add(choice);
            }
        }

        return neighbours;
    }

    private void visit(int x, int y) {
        mVisited[y][x] = true;
        --mVisitsLeft;
    }

    private boolean areUnvisitedCellsLeft() {
        return mVisitsLeft > 0;
    }
}
