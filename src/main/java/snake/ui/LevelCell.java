package snake.ui;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import snake.LevelController;
import snake.Main;
import snake.model.LevelData;

import java.io.IOException;

public class LevelCell extends AnchorPane {

    private final LevelData levelData;
    @FXML
    private Label levelNameLabel;
    @FXML
    private Label bestScoreValueLabel;
    @FXML
    private Button playButton;

    public LevelCell(LevelData levelData) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/level_cell.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        LevelPane levelPane = new LevelPane(6, 32, 24);
        this.levelData = levelData;
        levelPane.init(levelData);
        getChildren().add(0, levelPane);
        AnchorPane.setLeftAnchor(levelPane, 0.0);
        AnchorPane.setRightAnchor(levelPane, 0.0);
        AnchorPane.setTopAnchor(levelPane, 0.0);
        AnchorPane.setBottomAnchor(levelPane, 0.0);
        levelNameLabel.setText(levelData.getName());
        bestScoreValueLabel.setText(String.valueOf(levelData.getBestScore()));

        MenuItem deleteMenuItem = new MenuItem("Удалить");
        deleteMenuItem.setOnAction(event -> delete());
        ContextMenu contextMenu = new ContextMenu(deleteMenuItem);
        setOnContextMenuRequested(event -> {
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        });
    }

    private void delete() {
        this.levelData.delete();
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(event -> {
            ((Pane) this.getParent()).getChildren().remove(this);
        });
        st.play();
    }

    @FXML
    private void onPlay() throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/level.fxml"));
        Parent load = loader.load();
        LevelController controller = loader.getController();
        controller.initLevel(levelData);
        Main.setRoot(load);
    }

}
