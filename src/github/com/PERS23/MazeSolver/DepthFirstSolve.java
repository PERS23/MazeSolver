package github.com.PERS23.MazeSolver;

import java.awt.Point;
import java.util.*;

public class DepthFirstSolve implements SolvingStrategy {

    private Maze mCurrentMaze;
    private boolean[][] mVisited;
    private List<Point> mSolutionPath = new LinkedList<>();
    private List<Point> mAllPathsTaken = new LinkedList<>();

    @Override
    public void solve(Maze target, int startX, int startY, int endX, int endY) {
        mCurrentMaze = target;
        mVisited = new boolean[mCurrentMaze.getHeight()][mCurrentMaze.getWidth()];
        mSolutionPath.clear();
        mAllPathsTaken.clear();

        DFS(new Point(startX, startY), new Point(endX, endY));
    }

    private void DFS(Point start, Point end) {
        Map<Point, Point> backtrack = new HashMap<>();

        Deque<Point> depthStack = new ArrayDeque<>();
        depthStack.push(start);
        mVisited[start.y][start.x] = true;

        Point currentPoint = start;
        while (!depthStack.isEmpty() && !currentPoint.equals(end)) {
            currentPoint = depthStack.pop();
            mAllPathsTaken.add(currentPoint);

            for (Direction choice : Direction.values()) {
                if (!mCurrentMaze.isWall(currentPoint.x, currentPoint.y, choice)) {

                    Point oppositePoint = new Point(currentPoint.x + choice.getDX(),
                                                    currentPoint.y + choice.getDY());

                    if (mCurrentMaze.isWithinBounds(oppositePoint.x, oppositePoint.y) &&
                        !mVisited[oppositePoint.y][oppositePoint.x]) {

                        mVisited[oppositePoint.y][oppositePoint.x] = true;
                        depthStack.push(oppositePoint);
                        backtrack.put(oppositePoint, currentPoint);
                    }
                }
            }
        }

        buildPathFromBTMap(backtrack, start, end);
    }

    private void buildPathFromBTMap(Map<Point, Point> backtrack, Point start, Point end) {
        Point prev = backtrack.get(end);
        Point current = end;

        while (backtrack.containsKey(current)) {
            mSolutionPath.add(0, current);

            current = prev;
            prev = backtrack.get(prev);
        }
    }

    @Override
    public List<Point> getSolutionPath() {
        return mSolutionPath;
    }

    @Override
    public List<Point> getAllPathsTaken() {
        return mAllPathsTaken;
    }
}
