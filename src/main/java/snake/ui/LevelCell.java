package snake.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import snake.Main;

import java.io.IOException;

public class LevelCell extends AnchorPane {

    @FXML
    private Label levelNameLabel;

    public LevelCell(String title, LevelPane levelPane) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/level_cell.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        getChildren().add(0, levelPane);
        AnchorPane.setLeftAnchor(levelPane, 0.0);
        AnchorPane.setRightAnchor(levelPane, 0.0);
        AnchorPane.setTopAnchor(levelPane, 0.0);
        AnchorPane.setBottomAnchor(levelPane, 0.0);
        levelNameLabel.setText(title);
    }

}
