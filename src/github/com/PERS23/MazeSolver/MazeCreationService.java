package github.com.PERS23.MazeSolver;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MazeCreationService extends Service<Maze> {

    private GenStrategyPolicy mStrategyChoice;
    private int mWidth;
    private int mHeight;

    public GenStrategyPolicy getStrategyChoice() {
        return mStrategyChoice;
    }

    public void setStrategyChoice(GenStrategyPolicy strategyChoice) {
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
        final GenStrategyPolicy strategyChoice = mStrategyChoice;
        final int width = getWidth(), height = getHeight();

        return new Task<Maze>() {
            @Override
            protected Maze call() throws Exception {
                return Maze.randomMaze(width, height, strategyChoice);
            }
        };
    }
}
