package github.com.PERS23.MazeSolver;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MazeCreationService extends Service<Maze> {

    private GenPolicy mStrategyChoice;
    private int mWidth;
    private int mHeight;

    public GenPolicy getStrategyChoice() {
        return mStrategyChoice;
    }

    public void setStrategyChoice(GenPolicy strategyChoice) {
        mStrategyChoice = strategyChoice;
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
        final GenPolicy strategyChoice = mStrategyChoice;
        final int width = getWidth(), height = getHeight();

        return new Task<Maze>() {
            @Override
            protected Maze call() throws Exception {
                return Maze.randomMaze(width, height, strategyChoice);
            }
        };
    }
}
