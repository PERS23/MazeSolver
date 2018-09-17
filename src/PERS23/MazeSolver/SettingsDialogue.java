package PERS23.MazeSolver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsDialogue extends GridPane {

    private Settings mSettings;

    @FXML private TextField maze_width_entry;
    @FXML private TextField maze_height_entry;
    @FXML private TextField corridor_size_entry;
    @FXML private TextField wall_size_entry;
    @FXML private TextField starting_x_entry;
    @FXML private TextField starting_y_entry;
    @FXML private TextField ending_x_entry;
    @FXML private TextField ending_y_entry;

    public SettingsDialogue(Settings settings) {
        mSettings = settings;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SettingsDialogue.fxml"), ResourceBundle.getBundle("values/strings", Locale.ENGLISH));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        maze_width_entry.textProperty().bindBidirectional(mSettings.mazeWidthProperty(), new NumberStringConverter());
        maze_height_entry.textProperty().bindBidirectional(mSettings.mazeHeightProperty(), new NumberStringConverter());
        corridor_size_entry.textProperty().bindBidirectional(mSettings.corridorSizeProperty(), new NumberStringConverter());
        wall_size_entry.textProperty().bindBidirectional(mSettings.wallSizeProperty(), new NumberStringConverter());
        starting_x_entry.textProperty().bindBidirectional(mSettings.startXProperty(), new NumberStringConverter());
        starting_y_entry.textProperty().bindBidirectional(mSettings.startYProperty(), new NumberStringConverter());
        ending_x_entry.textProperty().bindBidirectional(mSettings.endXProperty(), new NumberStringConverter());
        ending_y_entry.textProperty().bindBidirectional(mSettings.endYProperty(), new NumberStringConverter());
    }

    @FXML
    private void handleOkButton(ActionEvent event) {
        closeStage(event);
    }

    private void closeStage(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
