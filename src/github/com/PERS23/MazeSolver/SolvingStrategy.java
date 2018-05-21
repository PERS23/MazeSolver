package github.com.PERS23.MazeSolver;

import java.awt.Point;
import java.util.List;

public interface SolvingStrategy {

    public void solve(Maze target, int startX, int startY, int endX, int endY);

    /**
     * Returns the list of X/Y points in order that must be taken to get from the start to the end.
     *
     * @return
     */
    public List<Point> getSolutionPath();

    /**
     * Returns the list of X/Y points in order that the algorithm tried
     *
     * @return
     */
    public List<Point> getAllPathsTaken();
}
