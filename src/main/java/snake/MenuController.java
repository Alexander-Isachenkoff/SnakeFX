package snake;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML
    private VBox root;

    @FXML
    void onExit() {
        root.getScene().getWindow().hide();
    }

    @FXML
    void onLevelEditor() {

    }

    @FXML
    void onPlay() {
        Main.toLevels();
    }

}
