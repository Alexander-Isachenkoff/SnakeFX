package snake.ui;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import snake.FileUtils;
import snake.model.LevelData;
import snake.model.Point;
import snake.model.Snake;

import java.util.*;

public class LevelPane extends StackPane {

    private final double gridSize;
    private final Map<Point, ObstacleNode> obstacles = new HashMap<>();
    private final Map<Point, FoodNode> foodNodes = new HashMap<>();
    private final List<SnakeSegmentNode> snakeNodes = new ArrayList<>();
    private final Pane obstaclesPane = new Pane();
    private final Pane snakePane = new Pane();
    private final Pane foodPane = new Pane();

    public LevelPane(double gridSize, int width, int height) {
        this.getChildren().addAll(obstaclesPane, foodPane, snakePane);
        this.gridSize = gridSize;
        this.setPrefWidth(gridSize * width);
        this.setPrefHeight(gridSize * height);
        this.setBackground(new Background(new BackgroundImage(FileUtils.loadImage("images/terrain.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        DropShadow effect = new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 5, 0, 2, 2);
        effect.setInput(new DropShadow(BlurType.THREE_PASS_BOX, Color.GRAY, 0, 0, 0, 5));
        obstaclesPane.setEffect(effect);

        foodPane.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 5, 0, 0, 0));
    }

    public void init(LevelData levelData) {
        for (Point obstacle : levelData.getObstacles()) {
            addObstacle(obstacle);
        }
    }

    public boolean hasObstacle(Point point) {
        return obstacles.containsKey(point);
    }

    public Set<Point> getObstacles() {
        return obstacles.keySet();
    }

    public void addObstacle(Point point) {
        ObstacleNode node = new ObstacleNode(gridSize);
        node.setTranslateX(point.getX() * gridSize);
        node.setTranslateY(point.getY() * gridSize);
        node.setScaleX(1.5);
        node.setScaleY(1.5);
        obstaclesPane.getChildren().add(0, node);
        obstacles.put(point, node);
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public void removeObstacle(Point point) {
        ObstacleNode node = obstacles.remove(point);
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(event -> obstaclesPane.getChildren().remove(node));
        st.play();
    }

    public void clearObstacles() {
        obstaclesPane.getChildren().removeAll(obstacles.values());
        obstacles.clear();
    }

    public void addFood(Point foodPoint) {
        FoodNode foodNode = FoodNode.random(gridSize);
        foodNode.setTranslateX(foodPoint.getX() * gridSize);
        foodNode.setTranslateY(foodPoint.getY() * gridSize);
        foodNode.setScaleX(0);
        foodNode.setScaleY(0);
        foodPane.getChildren().add(0, foodNode);
        foodNodes.put(foodPoint, foodNode);
        ScaleTransition st = new ScaleTransition(Duration.millis(500), foodNode);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public void removeFood(Point foodPoint) {
        Node node = foodNodes.remove(foodPoint);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(event -> foodPane.getChildren().remove(node));
        st.play();
    }

    public void clearFood() {
        foodPane.getChildren().removeAll(foodNodes.values());
        foodNodes.clear();
    }

    public void initSnake(Snake snake) {
        clearSnake();
        snake.getPoints().forEach(this::addSnakeSegment);
    }

    private void clearSnake() {
        snakePane.getChildren().removeAll(snakeNodes);
        snakeNodes.clear();
    }

    public void addSnakeSegment(Point point) {
        SnakeSegmentNode segmentNode = new SnakeSegmentNode(gridSize);
        segmentNode.setTranslateX(point.getX() * gridSize);
        segmentNode.setTranslateY(point.getY() * gridSize);
        snakePane.getChildren().add(segmentNode);
        snakeNodes.add(segmentNode);
    }

    public void moveSnake(Snake snake, double duration) {
        for (int i = 0; i < snake.getPoints().size(); i++) {
            Node node = snakeNodes.get(i);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(duration), node);
            tt.setInterpolator(Interpolator.LINEAR);
            Point point = snake.getPoints().get(i);
            tt.setToX(point.getX() * gridSize);
            tt.setToY(point.getY() * gridSize);
            tt.play();
        }
    }

}
