package snake.ui;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Duration;
import snake.FileUtils;
import snake.model.LevelMap;
import snake.model.Point;
import snake.model.Snake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelPane extends Pane {

    private final double gridSize;
    private final Map<Point, FoodNode> foodNodes = new HashMap<>();
    private final List<SnakeSegmentNode> snakeNodes = new ArrayList<>();

    public LevelPane(double gridSize, int width, int height) {
        this.gridSize = gridSize;
        this.setPrefWidth(gridSize * width);
        this.setPrefHeight(gridSize * height);
        this.setBackground(new Background(new BackgroundImage(FileUtils.loadImage("images/terrain.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public void init(LevelMap levelMap) {
        for (Point obstacle : levelMap.getObstacles()) {
            ObstacleNode node = new ObstacleNode(gridSize);
            node.setTranslateX(obstacle.getX() * gridSize);
            node.setTranslateY(obstacle.getY() * gridSize);
            getChildren().add(node);
        }
    }

    public void addFood(Point foodPoint) {
        FoodNode foodNode = FoodNode.random(gridSize);
        foodNode.setTranslateX(foodPoint.getX() * gridSize);
        foodNode.setTranslateY(foodPoint.getY() * gridSize);
        foodNode.setScaleX(0);
        foodNode.setScaleY(0);
        getChildren().add(0, foodNode);
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
        st.setOnFinished(event -> getChildren().remove(node));
        st.play();
    }

    public void clearFood() {
        getChildren().removeAll(foodNodes.values());
        foodNodes.clear();
    }

    public void initSnake(Snake snake) {
        clearSnake();
        snake.getPoints().forEach(this::addSnakeSegment);
    }

    private void clearSnake() {
        getChildren().removeAll(snakeNodes);
        snakeNodes.clear();
    }

    public void addSnakeSegment(Point point) {
        SnakeSegmentNode segmentNode = new SnakeSegmentNode(gridSize);
        segmentNode.setTranslateX(point.getX() * gridSize);
        segmentNode.setTranslateY(point.getY() * gridSize);
        getChildren().add(segmentNode);
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
