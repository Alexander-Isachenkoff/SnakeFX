package snake;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import snake.model.GameModel;
import snake.model.Snake;
import snake.ui.FoodNode;
import snake.ui.SnakeSegmentNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelController {

    private final GameModel gameModel = new GameModel();
    private final List<SnakeSegmentNode> snakeNodes = new ArrayList<>();
    private final Map<Point2D, FoodNode> foodNodes = new HashMap<>();
    private final double GRID_SIZE = 20;

    @FXML
    private Label scoreLabel;
    @FXML
    private AnchorPane gamePane;

    @FXML
    private void initialize() {
        gamePane.setPrefWidth(GRID_SIZE * gameModel.getWidth());
        gamePane.setPrefHeight(GRID_SIZE * gameModel.getHeight());

        gamePane.sceneProperty().addListener((observable, oldValue, newValue) -> {
            newValue.setOnKeyPressed(event -> {
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

        gameModel.scoreProperty().addListener((observable, oldValue, newValue) -> scoreLabel.setText(newValue.toString()));
        gameModel.setOnSegmentAdded(this::addSnakeSegment);
        gameModel.setOnSnakeMove(this::onSnakeMove);
        gameModel.setOnFoodAdded(this::onFoodAdded);
        gameModel.setOnFoodRemoved(this::onFoodRemoved);
        gameModel.setOnGameOver(this::onGameOver);

        restart();
    }

    private void restart() {
        gamePane.getChildren().removeAll(snakeNodes);
        snakeNodes.clear();
        gameModel.restart();
        for (Point2D point : gameModel.getSnake().getPoints()) {
            addSnakeSegment(point);
        }
    }

    private void onGameOver() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("fxml/game_over.fxml"));
        Parent load;
        try {
            load = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        gamePane.getChildren().add(load);
        AnchorPane.setTopAnchor(load, 0.0);
        AnchorPane.setBottomAnchor(load, 0.0);
        AnchorPane.setLeftAnchor(load, 0.0);
        AnchorPane.setRightAnchor(load, 0.0);

        GameOverController controller = loader.getController();

        controller.setOnMenu(this::onMenu);
        controller.setOnRestart(() -> {
            gamePane.getChildren().remove(load);
            restart();
        });
    }

    private void addSnakeSegment(Point2D point) {
        SnakeSegmentNode segmentNode = new SnakeSegmentNode(GRID_SIZE);
        segmentNode.setTranslateX(point.getX() * GRID_SIZE);
        segmentNode.setTranslateY(point.getY() * GRID_SIZE);
        gamePane.getChildren().add(segmentNode);
        snakeNodes.add(segmentNode);
    }

    private void onSnakeMove(Snake snake) {
        for (int i = 0; i < snake.getPoints().size(); i++) {
            Node node = snakeNodes.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(gameModel.getSpeed()), node);
            tt.setInterpolator(Interpolator.LINEAR);
            Point2D point = snake.getPoints().get(i);
            tt.setToX(point.getX() * GRID_SIZE);
            tt.setToY(point.getY() * GRID_SIZE);
            tt.play();
        }
    }

    private void onFoodAdded(Point2D foodPoint) {
        FoodNode foodNode = new FoodNode(GRID_SIZE);
        foodNode.setTranslateX(foodPoint.getX() * GRID_SIZE);
        foodNode.setTranslateY(foodPoint.getY() * GRID_SIZE);
        foodNode.setScaleX(0);
        foodNode.setScaleY(0);
        gamePane.getChildren().add(0, foodNode);
        foodNodes.put(foodPoint, foodNode);
        ScaleTransition st = new ScaleTransition(Duration.millis(500), foodNode);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    private void onFoodRemoved(Point2D foodPoint) {
        Node node = foodNodes.remove(foodPoint);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(event -> gamePane.getChildren().remove(node));
        st.play();
    }

    @FXML
    private void onMenu() {

    }

}
