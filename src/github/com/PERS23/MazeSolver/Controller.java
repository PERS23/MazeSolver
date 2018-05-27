package github.com.PERS23.MazeSolver;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.util.Pair;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    private static final int BASE_ANIMATION_SPEED_MS = 50;

    private ControllerSettings mSettings;
    // Variables regarding the maze and its generation/solution
    private GenPolicy mCurrentGenStrategy;
    private MazeCreationService mCreationService;
    private Maze mCurrentMaze;
    private SolvingPolicy mCurrentSolvingStrategy;
    private MazeSolvingService mSolutionService;

    private ListIterator<Point> mPathsListIterator;      // Iterator that represents what point we've placed in the list
    private List<Point> mPathsTaken;  // Solution comes after all paths taken, done so don't have to mess around with switching iterators half way through

    private final ImageView mStepBack;
    private final ImageView mStop;
    private final ImageView mPlay;
    private final ImageView mPause;
    private final ImageView mStepForward;

    private int mSolutionStartIndex;     // Pivot point as to where the animation should start highlighting the solution
    private MazeImageBuilder mMazeImageBuilder;
    private Timeline mSolutionAnimation;
    private boolean mIsAnimationOver;

    @FXML private BorderPane root;
    @FXML private ComboBox<GenPolicy> gen_choices;
    @FXML private ComboBox<SolvingPolicy> solve_choices;
    @FXML private Button solve_maze_button;
    @FXML private HBox playback_controls;
    @FXML private Button step_backward;
    @FXML private Button stop;
    @FXML private Button play_pause;
    @FXML private Button step_forward;
    @FXML private HBox speed_controls;
    @FXML private Slider speed_slider;
    @FXML private Label current_speed_indicator;

    public Controller() {
        mCurrentSolvingStrategy = SolvingPolicy.DFS;
        mCurrentGenStrategy = GenPolicy.RECURSIVE_BACKTRACK;

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
        speed_slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double scaledDown = ((Double) newValue) / 100;
            current_speed_indicator.setText(String.format("%.2fx", scaledDown));
            mSolutionAnimation.setRate(scaledDown);
        });

        gen_choices.getItems().addAll(GenPolicy.values());
        gen_choices.getSelectionModel().selectFirst();
        gen_choices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mCurrentGenStrategy = newValue;
            mCreationService.setStrategyChoice(mCurrentGenStrategy);
        });

        solve_choices.getItems().addAll(SolvingPolicy.values());
        solve_choices.getSelectionModel().selectFirst();
        solve_choices.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            mCurrentSolvingStrategy = newValue;
            mSolutionService.setStrategyChoice(mCurrentSolvingStrategy);
        });

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
            mMazeImageBuilder = new MazeImageBuilder(mCurrentMaze, mSettings.getWallSize(), mSettings.getCorridorSize());
            root.setCenter(mMazeImageBuilder.getCanvas());
            solve_maze_button.setDisable(false);
        });

        mSolutionService = new MazeSolvingService();
        mSolutionService.setOnSucceeded(event -> {
            Pair<List<Point>, List<Point>> result = mSolutionService.getValue();

            mPathsTaken = result.getKey();
            mSolutionStartIndex = mPathsTaken.size() - 1;
            mPathsTaken.addAll(result.getValue());
            resetAnimation();

            mSolutionAnimation.setCycleCount(mPathsTaken.size());

            enablePlaybackControls();                      // Only enable playback once the solution thread has finished
        });

        /* Animation is done with 1 keyframe that repeats over and over, this key frame has a command class attached
         * which has its handle method called after the frame is over. This handle method moves the iterator forward
         * and calls the highlight method on the maze image builder with the x/y point returned by the iterators next().
         * Animations cycle count is set to the length of the returned list, and is recalculated if the user steps
         * forward/back so the animation doesn't perform any otherwise wasted cycles.
         */
        mSolutionAnimation = new Timeline();
        mSolutionAnimation.getKeyFrames().add(new KeyFrame(Duration.millis(BASE_ANIMATION_SPEED_MS), action -> {
            stepForwardBuildProcess();
        }));
        mSolutionAnimation.setOnFinished(e -> {
            resetToPlayIcon();
            mIsAnimationOver = true;                // Explicitly mark as ended so as to support replay function correctly
        });
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

    @FXML
    public void togglePlayPause() {
        if (mSolutionAnimation.getStatus() == Animation.Status.RUNNING) {
            pauseAnimation();
        } else {
            playAnimation();
        }
    }

    private void playAnimation() {
        if (mIsAnimationOver) {                               // If animation has ended, and user clicks play restart it
            mMazeImageBuilder.reset();
            resetAnimation();
            mIsAnimationOver = false;
        }
        mSolutionAnimation.play();
        play_pause.setGraphic(mPause);
    }

    // Resets the cycle count back to the length, and puts the iterator back to the start of the list
    private void resetAnimation() {
        if (mPathsTaken != null) {
            mSolutionAnimation.setCycleCount(mPathsTaken.size());
            mPathsListIterator = mPathsTaken.listIterator();
        }
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
        resetAnimation();
        resetToPlayIcon();
        mMazeImageBuilder.reset();
    }

    @FXML
    public void stepForwardAnimation() {
        mSolutionAnimation.stop();                                                                     // Stop animation
        resetToPlayIcon();
        stepForwardBuildProcess();
                                                                                          // Recalculate the cycles left
        mSolutionAnimation.setCycleCount(mPathsTaken.size() - mPathsListIterator.nextIndex());
    }

    private void stepForwardBuildProcess() {
        if (mPathsListIterator != null && mPathsListIterator.hasNext()) {
            int nextIndex = mPathsListIterator.nextIndex();
            Point next = mPathsListIterator.next();
                                  // If the next one you're going to is beyond the solution start, highlight as solution
            if (nextIndex > mSolutionStartIndex) {
                mMazeImageBuilder.highlightSolutionPoint(next.x, next.y);
            } else {                 // If the iterator is not past the point where solution starts, highlight as normal
                mMazeImageBuilder.highlightNormalPoint(next.x, next.y);
            }
        }
    }

    @FXML
    public void stepBackAnimation() {
        mSolutionAnimation.stop();
        resetToPlayIcon();
        stepBackBuildProcess();
        mSolutionAnimation.setCycleCount(mPathsTaken.size() - mPathsListIterator.nextIndex());
    }

    private void stepBackBuildProcess() {
        if (mPathsListIterator != null && mPathsListIterator.hasPrevious()) {
            int prevIndex = mPathsListIterator.previousIndex();
            Point prev = mPathsListIterator.previous();

            if (prevIndex <= mSolutionStartIndex) {
                mMazeImageBuilder.unhighlightNormalPoint(prev.x, prev.y);
            } else {
                mMazeImageBuilder.unhighlightSolutionPoint(prev.x, prev.y);
            }

            if (mIsAnimationOver) { // Reset this so hitting play if stepped back from end doesn't restart
                mIsAnimationOver = false;
            }
        }
    }

    @FXML
    public void handleNewMazeButton() {
        mSolutionAnimation.stop();                                                         // Immediately stop animation
        solve_maze_button.setDisable(true);
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
        mPathsListIterator = null;
    }

    @FXML
    public void handleSolveMazeButton() {
        stopAnimation();
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
