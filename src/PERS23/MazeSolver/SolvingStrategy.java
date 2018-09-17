package PERS23.MazeSolver;

import javafx.util.Pair;

import java.awt.Point;
import java.util.List;

public interface SolvingStrategy {

    /**
     * Solves the target maze and returns a pair of lists, which contain 1. all the paths taken, 2. the solution path.
     *
     * @param target Target maze the algorithm will be trying to solve.
     * @param start Starting X/Y postion, must be within the target maze.
     * @param end Goal X/Y postion, must be within the target maze.
     * @return Pair of lists. Key is a list of all the points the search went down in order. Value is a list of all the
     * points for the solution that the algorithm delivered, in order. If there's no path from the start to the end,
     * the Value will be empty.
     */
    public Pair<List<Point>, List<Point>> solve(Maze target, Point start, Point end);
}
