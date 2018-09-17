package PERS23.MazeSolver;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.awt.Point;
import java.util.List;

public class MazeSolvingService extends Service<Pair<List<Point>, List<Point>>> {

    private Point mStartPoint, mEndPoint;
    private Maze mTarget;
    private SolvingPolicy mStrategyChoice;

    public Maze getTarget() {
        return mTarget;
    }

    public void setTarget(Maze target) {
        mTarget = target;
    }

    public SolvingPolicy getStrategyChoice() {
        return mStrategyChoice;
    }

    public void setStrategyChoice(SolvingPolicy strategyChoice) {
        mStrategyChoice = strategyChoice;
    }

    public void setPoints(Point startPoint, Point endPoint) {
        mStartPoint = startPoint;
        mEndPoint = endPoint;
    }

    public Point getStartPoint() {
        return mStartPoint;
    }

    public Point getEndPoint() {
        return mEndPoint;
    }

    @Override
    protected Task<Pair<List<Point>, List<Point>>> createTask() {
        final Maze target = getTarget();
        final SolvingPolicy strategy = getStrategyChoice();
        final Point start = getStartPoint(), end = getEndPoint();

        return new Task<Pair<List<Point>, List<Point>>>() {
            @Override
            protected Pair<List<Point>, List<Point>> call() throws Exception {
                return strategy.getSolver().solve(target, start, end);
            }
        };
    }
}
