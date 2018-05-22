package github.com.PERS23.MazeSolver;

import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;

import java.awt.Point;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private GenerationStrategy mGenerationStrategy;
    private MazeCreationService mCreationService;
    private Maze mCurrentMaze;

    private SolvingStrategy mSolvingStrategy;
    private MazeSolvingService mSolutionService;
    private List<Point> mAllPathsTaken;
    private List<Point> mSolutionPath;

    private AnimationBuilder mCurrentAnimator;
    private Timeline mSolutionAnimation;

    @FXML private BorderPane root;
    @FXML private Button new_maze_button;
    @FXML private Button solve_maze_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mSolvingStrategy = new DepthFirstSolve();
        mGenerationStrategy = new RecursiveBacktrack();

        mCreationService = new MazeCreationService();
        mCreationService.setOnSucceeded(event -> {
            mCurrentMaze = mCreationService.getValue();
            mCurrentAnimator = new AnimationBuilder(mCurrentMaze, 8, 16);
            root.setCenter(mCurrentAnimator.getCanvas());
        });

        mSolutionService = new MazeSolvingService();
        mSolutionService.setOnSucceeded(event -> {
            Pair<List<Point>, List<Point>> result = mSolutionService.getValue();
            mAllPathsTaken = result.getKey();
            mSolutionPath = result.getValue();
        });

        new_maze_button.setOnAction(e -> {
            createNewMaze(20, 20);
        });

        solve_maze_button.setOnAction(e -> {
            solveCurrentMaze(0, 0, 19, 19);
        });
    }

    public void createNewMaze(int width, int height) {
        mAllPathsTaken = null;
        mSolutionPath = null;

        if (mGenerationStrategy != mCreationService.getMazeGenerator()) {
            mCreationService.setMazeGenerator(mGenerationStrategy);
        }
        mCreationService.setWidth(width);
        mCreationService.setHeight(height);
        mCreationService.restart();
    }

    public void solveCurrentMaze(int startX, int startY, int endX, int endY) {
        if (mCurrentMaze != mSolutionService.getTarget()) {
            mSolutionService.setTarget(mCurrentMaze);
        }

        if (mSolvingStrategy != mSolutionService.getSolver()) {
            mSolutionService.setSolver(mSolvingStrategy);
        }

        mSolutionService.setPoints(new Point(startX, startY), new Point(endX, endY));
        mSolutionService.restart();
    }
}
