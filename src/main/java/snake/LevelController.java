package snake;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import snake.model.GameModel;
import snake.model.LevelMap;
import snake.ui.LevelPane;

import java.io.IOException;

public class LevelController {

    private final double GRID_SIZE = 20;
    private final GameModel gameModel = new GameModel();
    private final LevelPane gamePane = new LevelPane(GRID_SIZE, 32, 24);
    @FXML
    private Label scoreLabel;
    @FXML
    private AnchorPane gamePaneWrapper;

    @FXML
    private void initialize() {
        gameModel.scoreProperty().addListener((observable, oldValue, newValue) -> scoreLabel.setText(newValue.toString()));
        gameModel.setOnSegmentAdded(gamePane::addSnakeSegment);
        gameModel.setOnSnakeMove(snake -> gamePane.moveSnake(snake, gameModel.getSpeed()));
        gameModel.setOnFoodAdded(gamePane::addFood);
        gameModel.setOnFoodRemoved(gamePane::removeFood);
        gameModel.setOnGameOver(this::onGameOver);

        gamePane.sceneProperty().addListener((observable, oldValue, scene) -> {
            if (scene == null) {
                return;
            }
            scene.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case UP:
                        gameModel.getSnake().turnUp();
                        break;
                    case DOWN:
                        gameModel.getSnake().turnDown();
                        break;
                    case LEFT:
                        gameModel.getSnake().turnLeft();
                        break;
                    case RIGHT:
                        gameModel.getSnake().turnRight();
                        break;
                }
            });
        });
        gamePaneWrapper.getChildren().setAll(gamePane);
        AnchorPane.setLeftAnchor(gamePane, 0.0);
        AnchorPane.setRightAnchor(gamePane, 0.0);
        AnchorPane.setTopAnchor(gamePane, 0.0);
        AnchorPane.setBottomAnchor(gamePane, 0.0);
    }

    public void initLevel(LevelMap levelMap) {
        gameModel.init(levelMap);
        gamePane.init(levelMap);
        restart();
    }

    private void restart() {
        gamePane.clearFood();
        gameModel.restart();
        gamePane.initSnake(gameModel.getSnake());
    }

    private void onGameOver() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/game_over.fxml"));
        Parent load;
        try {
            load = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gamePaneWrapper.getChildren().add(load);
        AnchorPane.setTopAnchor(load, 0.0);
        AnchorPane.setBottomAnchor(load, 0.0);
        AnchorPane.setLeftAnchor(load, 0.0);
        AnchorPane.setRightAnchor(load, 0.0);

        GameOverController controller = loader.getController();

        controller.setOnMenu(this::onMenu);
        controller.setOnRestart(() -> {
            gamePaneWrapper.getChildren().remove(load);
            restart();
        });
    }

    @FXML
    private void onMenu() {
        gameModel.stop();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/levels.fxml"));
        try {
            Main.setRoot(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
