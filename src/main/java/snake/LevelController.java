package snake;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import snake.model.GameModel;
import snake.model.Snake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelController {

    private final GameModel gameModel = new GameModel();
    private final List<Node> snakeNodes = new ArrayList<>();
    private final Map<Point2D, Node> foodNodes = new HashMap<>();
    private final double GRID_SIZE = 16;
    @FXML
    private Pane gamePane;

    @FXML
    private void initialize() {
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

        gameModel.setOnSegmentAdded(this::addSnakeSegment);
        gameModel.setOnSnakeMove(this::onSnakeMove);
        gameModel.setOnFoodAdded(this::onFoodAdded);
        gameModel.setOnFoodRemoved(this::onFoodRemoved);

        for (Point2D point : gameModel.getSnake().getPoints()) {
            addSnakeSegment(point);
        }

        gameModel.start();
    }

    private void addSnakeSegment(Point2D point) {
        Circle circle = new Circle(GRID_SIZE / 2, Color.RED);
        circle.setTranslateX(point.getX() * GRID_SIZE);
        circle.setTranslateY(point.getY() * GRID_SIZE);
        gamePane.getChildren().add(circle);
        snakeNodes.add(circle);
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
        Circle foodCircle = new Circle(GRID_SIZE / 2, Color.LAWNGREEN);
        foodCircle.setTranslateX(foodPoint.getX() * GRID_SIZE);
        foodCircle.setTranslateY(foodPoint.getY() * GRID_SIZE);
        foodCircle.setScaleX(0);
        foodCircle.setScaleY(0);
        gamePane.getChildren().add(0, foodCircle);
        foodNodes.put(foodPoint, foodCircle);
        ScaleTransition st = new ScaleTransition(Duration.millis(500), foodCircle);
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
}
