package github.com.PERS23.MazeSolver;

import javafx.util.Pair;

import java.awt.Point;
import java.util.*;

public class BreadthFirstSolve implements SolvingStrategy {

    @Override
    public Pair<List<Point>, List<Point>> solve(Maze target, Point start, Point end) {
        return BFS(target, start, end);
    }

    private Pair<List<Point>, List<Point>> BFS(Maze target, Point start, Point end) {
        boolean[][] visits = new boolean[target.getHeight()][target.getWidth()];
        Map<Point, Point> backtrack = new HashMap<>();

        List<Point> allPathsTaken = new LinkedList<>();

        Queue<Point> breadthQueue = new LinkedList<>();
        breadthQueue.add(start);
        visits[start.y][start.x] = true;

        Point currentPoint = start;
        while (!breadthQueue.isEmpty() && !currentPoint.equals(end)) {
            currentPoint = breadthQueue.poll();
            allPathsTaken.add(currentPoint);

            for (Direction choice : Direction.values()) {
                if (!target.isWall(currentPoint.x, currentPoint.y, choice)) {

                    Point oppositePoint = new Point(currentPoint.x + choice.getDX(),
                            currentPoint.y + choice.getDY());

                    if (target.isWithinBounds(oppositePoint.x, oppositePoint.y) &&
                            !visits[oppositePoint.y][oppositePoint.x]) {

                        visits[oppositePoint.y][oppositePoint.x] = true;
                        breadthQueue.add(oppositePoint);
                        backtrack.put(oppositePoint, currentPoint);
                    }
                }
            }
        }

        return new Pair<>(allPathsTaken, buildPathFromBTMap(backtrack, start, end));
    }

    private List<Point> buildPathFromBTMap(Map<Point, Point> backtrack, Point start, Point end) {
        List<Point> solutionPath = new LinkedList<>();

        Point prev = backtrack.get(end);
        Point current = end;

        while (backtrack.containsKey(current)) {
            solutionPath.add(0, current);

            current = prev;
            prev = backtrack.get(prev);
        }

        if (!solutionPath.isEmpty()) {
            solutionPath.add(0, start);
        }

        return solutionPath;
    }
}
