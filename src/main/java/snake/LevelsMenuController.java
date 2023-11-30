package snake;

import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.util.Duration;
import snake.model.LevelData;
import snake.ui.LevelCell;

public class LevelsMenuController {

    @FXML
    private FlowPane levelsPane;

    @FXML
    private void initialize() {
        LevelData.loadAllAsync(levelData -> {
            LevelCell cell = new LevelCell(levelData);
            cell.setScaleX(0);
            Platform.runLater(() -> {
                levelsPane.getChildren().add(cell);
                ScaleTransition st = new ScaleTransition(Duration.millis(200), cell);
                st.setToX(1);
                st.play();
            });
        });
    }

    @FXML
    private void onMenu() {
        Main.toMainMenu();
    }

}
