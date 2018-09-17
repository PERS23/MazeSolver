package PERS23.MazeSolver;

/* http://www.astrolog.org/labyrnth/algrithm.htm
 * To create a Maze without dead ends, basically add wall segments throughout the Maze at random, but ensure that each
 * new segment added will not cause a dead end to be made. I make them with four steps:
 * (1) Start with the outer wall,
 * (2) Loop through the Maze and add single wall segments touching each wall vertex to ensure there are no open
 *     rooms or small "pole" walls in the Maze,
 * (3) Loop over all possible wall segments in random order, adding a wall there if it wouldn't cause a dead end,
 * (4) Either run the isolation remover utility at the end to make a legal Maze that has a solution, or be smarter in
 *     step three and make sure a wall is only added if it also wouldn't cause an isolated section.
 */
public class BraidMazeGenerator implements GenStrategy {

    @Override
    public Maze generateRandomMaze(int width, int height) {
        return null;
    }

}
