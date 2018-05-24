package github.com.PERS23.MazeSolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.awt.Point;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private ControllerSettings mSettings;

    private GenStrategyPolicy mCurrentGenStrategy;
    private MazeCreationService mCreationService;
    private Maze mCurrentMaze;
    private SolvingStrategyPolicy mCurrentSolvingStrategy;
    private MazeSolvingService mSolutionService;

    private ListIterator<Point> mPathPosition;
    private List<Point> mPathsTaken;                                                             // Solution comes after

    private final ImageView mStepBack;
    private final ImageView mStop;
    private final ImageView mPlay;
    private final ImageView mPause;
    private final ImageView mStepForward;

    private AnimationBuilder mCurrentAnimator;
    private Timeline mSolutionAnimation;

    @FXML private BorderPane root;
    @FXML private ComboBox<GenStrategyPolicy> gen_choices;
    @FXML private ComboBox<SolvingStrategyPolicy> solve_choices;
    @FXML private HBox playback_controls;
    @FXML private Button step_backward;
    @FXML private Button stop;
    @FXML private Button play_pause;
    @FXML private Button step_forward;
    @FXML private HBox speed_controls;

    public Controller() {
        mCurrentSolvingStrategy = SolvingStrategyPolicy.DFS;
        mCurrentGenStrategy = GenStrategyPolicy.RECURSIVE_BACKTRACK;

        mSettings = new ControllerSettings();

        mStepBack = new ImageView(new Image("img/ic_step_back.png"));
        mStepBack.setFitHeight(30);
        mStepBack.setFitWidth(30);

        mStop = new ImageView(new Image("img/ic_stop.png"));
        mStop.setFitHeight(30);
        mStop.setFitWidth(30);

        mPlay = new ImageView(new Image("img/ic_play.png"));
        mPlay.setFitHeight(30);
        mPlay.setFitWidth(30);

        mPause = new ImageView(new Image("img/ic_pause.png"));
        mPause.setFitHeight(30);
        mPause.setFitWidth(30);

        mStepForward = new ImageView(new Image("img/ic_step_forward.png"));
        mStepForward.setFitHeight(30);
        mStepForward.setFitWidth(30);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gen_choices.getItems().addAll(GenStrategyPolicy.values());
        gen_choices.getSelectionModel().selectFirst();
        solve_choices.getItems().addAll(SolvingStrategyPolicy.values());
        solve_choices.getSelectionModel().selectFirst();

        step_backward.setGraphic(mStepBack);
        step_backward.setBackground(Background.EMPTY);

        stop.setGraphic(mStop);
        stop.setBackground(Background.EMPTY);

        play_pause.setGraphic(mPlay);
        play_pause.setBackground(Background.EMPTY);

        step_forward.setGraphic(mStepForward);
        step_forward.setBackground(Background.EMPTY);

        disablePlaybackControls();

        mCreationService = new MazeCreationService();
        mCreationService.setOnSucceeded(event -> {
            mCurrentMaze = mCreationService.getValue();
            mCurrentAnimator = new AnimationBuilder(mCurrentMaze, mSettings.getWallSize(), mSettings.getCorridorSize());
            root.setCenter(mCurrentAnimator.getCanvas());
        });

        mSolutionService = new MazeSolvingService();
        mSolutionService.setOnSucceeded(event -> {
            Pair<List<Point>, List<Point>> result = mSolutionService.getValue();

            mPathsTaken = result.getKey();
            mPathsTaken.addAll(result.getValue());
            putAnimationPositionToStart();

            mSolutionAnimation.setCycleCount(mPathsTaken.size());

            enablePlaybackControls();
        });

        mSolutionAnimation = new Timeline();
        mSolutionAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(50), action -> {
            if (mPathPosition != null && mPathPosition.hasNext()) {
                Point currentPoint = mPathPosition.next();
                mCurrentAnimator.highlight(currentPoint.x, currentPoint.y);
            }
        }));
        mSolutionAnimation.setOnFinished(e -> {
            play_pause.setGraphic(mPlay);
        });
    }

    private void putAnimationPositionToStart() {
        mPathPosition = mPathsTaken.listIterator();
    }

    private void enablePlaybackControls() {
        for (Node child : playback_controls.getChildren()) {
            child.setDisable(false);
        }
        for (Node child : speed_controls.getChildren()) {
            child.setDisable(false);
        }
    }

    private void disablePlaybackControls() {
        for (Node child : playback_controls.getChildren()) {
            child.setDisable(true);
        }
        for (Node child : speed_controls.getChildren()) {
            child.setDisable(true);
        }
    }

    private void setAnimationRate(double animationRate)  {
        mSolutionAnimation.setRate(animationRate);
    }

    @FXML
    public void togglePlayPause() {
        if (mSolutionAnimation.getStatus() == Animation.Status.RUNNING) {
            pauseAnimation();
        } else {
            playAnimation();
        }
    }

    private void playAnimation() {
        if (mSolutionAnimation.getStatus() == Animation.Status.STOPPED) {
            clearAllAnimationHighlights();
            putAnimationPositionToStart();
        }
        mSolutionAnimation.play();
        play_pause.setGraphic(mPause);
    }

    private void pauseAnimation() {
        mSolutionAnimation.pause();
        resetToPlayIcon();
    }

    private void resetToPlayIcon() {
        play_pause.setGraphic(mPlay);
    }

    @FXML
    public void stopAnimation() {
        mSolutionAnimation.stop();
        resetToPlayIcon();
        clearAllAnimationHighlights();
    }

    private void clearAllAnimationHighlights() {
        for (Point p : mPathsTaken) {
            mCurrentAnimator.unhighlight(p.x, p.y);
        }
    }

    private void stepBackAnimation() {

    }

    private void stepForwardAnimation() {

    }

    @FXML
    public void handleNewMazeButton() {
        mSolutionAnimation.stop();
        resetToPlayIcon();
        disablePlaybackControls();

        clearSolutionPathLists();
        createNewMaze(mSettings.getMazeWidth(),mSettings.getMazeHeight());
    }

    private void createNewMaze(int width, int height) {
        if (mCurrentGenStrategy != mCreationService.getStrategyChoice()) {
            mCreationService.setStrategyChoice(mCurrentGenStrategy);
        }
        mCreationService.setWidth(width);
        mCreationService.setHeight(height);
        mCreationService.restart();
    }

    private void clearSolutionPathLists() {
        mPathsTaken = null;
        mPathPosition = null;
    }

    @FXML
    public void handleSolveMazeButton() {
        disablePlaybackControls();
        solveCurrentMaze(mSettings.getStartPoint(), mSettings.getEndPoint());
    }

    private void solveCurrentMaze(Point start, Point end) {
        if (mCurrentMaze != mSolutionService.getTarget()) {   // If current maze is not equal to what service is holding
            mSolutionService.setTarget(mCurrentMaze);
        }
                                                  // If the current strategy is not equal to what the service is holding
        if (mCurrentSolvingStrategy != mSolutionService.getStrategyChoice()) {
            mSolutionService.setStrategyChoice(mCurrentSolvingStrategy);
        }

        mSolutionService.setPoints(start, end);                                              // Update the target points
        mSolutionService.restart();
    }
}
