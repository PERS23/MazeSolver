package github.com.PERS23.MazeSolver;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.util.Pair;

import java.awt.Point;
import java.util.List;

public class MazeSolvingService extends Service<Pair<List<Point>, List<Point>>> {

    private Point mStartPoint, mEndPoint;
    private Maze mTarget;
    private SolvingStrategy mSolver;

    public Maze getTarget() {
        return mTarget;
    }

    public void setTarget(Maze target) {
        mTarget = target;
    }

    public SolvingStrategy getSolver() {
        return mSolver;
    }

    public void setSolver(SolvingStrategy solver) {
        mSolver = solver;
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
        final SolvingStrategy solver = getSolver();
        final Point start = getStartPoint(), end = getEndPoint();

        return new Task<Pair<List<Point>, List<Point>>>() {
            @Override
            protected Pair<List<Point>, List<Point>> call() throws Exception {
                solver.solve(target, start, end);
                return new Pair<>(solver.getAllPathsTaken(), solver.getSolutionPath());
            }
        };
    }
}
