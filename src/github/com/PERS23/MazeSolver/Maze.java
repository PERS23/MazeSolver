package github.com.PERS23.MazeSolver;

import java.util.Random;

public class Maze {

    private MazeCell[][] mLabyrinth; // [row][col], [y][x]

    public static Maze randomMaze(int width, int height) {
        GenerationStrategy generator = new RecursiveBacktrack();
        return generator.generateRandomMaze(width, height);
    }

    public Maze(int width, int height) {
        mLabyrinth = new MazeCell[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mLabyrinth[y][x] = new MazeCell();
            }
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

    public void carveWall(int x, int y, Direction choice) {
        if (isWithinBounds(x + choice.getX(), y + choice.getY())) {
            mLabyrinth[y][x].carve(choice);
            mLabyrinth[y + choice.getY()][x + choice.getX()].carve(choice.opposite());
        }
    }
}
