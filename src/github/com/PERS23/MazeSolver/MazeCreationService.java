package github.com.PERS23.MazeSolver;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MazeCreationService extends Service<Maze> {

    private GenerationStrategy mMazeGenerator;
    private int mWidth;
    private int mHeight;

    public GenerationStrategy getMazeGenerator() {
        return mMazeGenerator;
    }

    public void setMazeGenerator(GenerationStrategy mazeGenerator) {
        mMazeGenerator = mazeGenerator;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    @Override
    protected Task<Maze> createTask() {
        final GenerationStrategy mazeGenerator = getMazeGenerator();
        final int width = getWidth(), height = getHeight();

        return new Task<Maze>() {
            @Override
            protected Maze call() throws Exception {
                return Maze.randomMaze(width, height, mazeGenerator);
            }
        };
    }
}
