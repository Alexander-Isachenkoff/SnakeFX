package snake.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import snake.LevelController;
import snake.Main;
import snake.model.LevelMap;

import java.io.IOException;

public class LevelCell extends AnchorPane {

    private final LevelMap levelMap;
    @FXML
    private Label levelNameLabel;

    public LevelCell(String title, LevelMap levelMap) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/level_cell.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LevelPane levelPane = new LevelPane(6, 32, 24);
        this.levelMap = levelMap;
        levelPane.init(levelMap);
        getChildren().add(0, levelPane);
        AnchorPane.setLeftAnchor(levelPane, 0.0);
        AnchorPane.setRightAnchor(levelPane, 0.0);
        AnchorPane.setTopAnchor(levelPane, 0.0);
        AnchorPane.setBottomAnchor(levelPane, 0.0);
        levelNameLabel.setText(title);
    }

    @FXML
    private void onPlay() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/main.fxml"));
        Parent load = loader.load();
        LevelController controller = loader.getController();
        controller.initLevel(levelMap);
        Main.setRoot(load);
    }

}
