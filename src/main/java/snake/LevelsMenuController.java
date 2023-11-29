package snake;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import snake.model.LevelData;
import snake.ui.LevelCell;

import java.util.List;
import java.util.stream.Collectors;

public class LevelsMenuController {

    @FXML
    private FlowPane levelsPane;

    @FXML
    private void initialize() {
        List<LevelCell> levelCells = LevelData.load().stream()
                .map(levelData -> new LevelCell(levelData.getName(), levelData))
                .collect(Collectors.toList());
        levelsPane.getChildren().setAll(levelCells);
    }

}
