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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LevelPane extends StackPane {

    private final double gridSize;
    private final List<GameNode> gameNodes = new ArrayList<>();
    private final Pane obstaclesPane = new Pane();
    private final Pane snakePane = new Pane();
    private final Pane foodPane = new Pane();

    public LevelPane(double gridSize, int width, int height) {
        this.getChildren().addAll(obstaclesPane, foodPane, snakePane);
        this.gridSize = gridSize;
        this.setPrefWidth(gridSize * width);
        this.setPrefHeight(gridSize * height);
        this.setBackground(new Background(new BackgroundImage(FileUtils.loadImage("images/terrain.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));

        DropShadow effect = new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 5, 0, 2, 0);
        effect.setInput(new DropShadow(BlurType.THREE_PASS_BOX, Color.GRAY, 0, 0, 0, 5));
        obstaclesPane.setEffect(effect);

        foodPane.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 5, 0, 0, 0));
        snakePane.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK, 5, 0, 0, 0));
    }

    public void init(LevelData levelData) {
        for (Point obstacle : levelData.getObstacles()) {
            addObstacle(obstacle);
        }
    }

    public boolean hasObstacle(Point point) {
        return getNode(ObstacleNode.class, point) != null;
    }

    public boolean hasSnakeSegment(Point point) {
        return getNode(SnakeSegmentNode.class, point) != null;
    }

    public Set<Point> getObstacles() {
        return getNodes(ObstacleNode.class).stream()
                .map(GameNode::getPoint)
                .collect(Collectors.toSet());
    }

    public void addObstacle(Point point) {
        ObstacleNode node = new ObstacleNode(gridSize);
        node.setTranslateX(point.getX() * gridSize);
        node.setTranslateY(point.getY() * gridSize);
        node.setScaleX(1.5);
        node.setScaleY(1.5);
        obstaclesPane.getChildren().add(0, node);
        node.setPoint(point);
        gameNodes.add(node);
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public void removeObstacle(Point point) {
        ObstacleNode node = getNode(ObstacleNode.class, point);
        gameNodes.remove(node);
        ScaleTransition st = new ScaleTransition(Duration.millis(100), node);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(event -> obstaclesPane.getChildren().remove(node));
        st.play();
    }

    public void clearObstacles() {
        removeAll(ObstacleNode.class);
    }

    public void addFood(Point foodPoint) {
        FoodNode foodNode = FoodNode.random(gridSize);
        foodNode.setTranslateX(foodPoint.getX() * gridSize);
        foodNode.setTranslateY(foodPoint.getY() * gridSize);
        foodNode.setScaleX(0);
        foodNode.setScaleY(0);
        foodPane.getChildren().add(0, foodNode);
        foodNode.setPoint(foodPoint);
        gameNodes.add(foodNode);
        ScaleTransition st = new ScaleTransition(Duration.millis(500), foodNode);
        st.setToX(1);
        st.setToY(1);
        st.play();
    }

    public void removeFood(Point point) {
        FoodNode foodNode = getNode(FoodNode.class, point);
        gameNodes.remove(foodNode);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), foodNode);
        st.setToX(0);
        st.setToY(0);
        st.setOnFinished(event -> foodPane.getChildren().remove(foodNode));
        st.play();
    }

    private <T extends GameNode> T getNode(Class<T> type, Point point) {
        return getNodes(type).stream()
                .filter(node -> node.getPoint().equals(point))
                .findFirst()
                .orElse(null);
    }

    private <T extends GameNode> List<T> getNodes(Class<T> type) {
        return gameNodes.stream()
                .filter(type::isInstance)
                .map(gameNode -> (T) gameNode)
                .collect(Collectors.toList());
    }

    public void clearFood() {
        removeAll(FoodNode.class);
    }

    private <T extends GameNode> void removeAll(Class<T> type) {
        List<T> nodes = getNodes(type);
        nodes.forEach(t -> ((Pane) t.getParent()).getChildren().remove(t));
        gameNodes.removeAll(nodes);
    }

    public void initSnake(Snake snake) {
        clearSnake();
        snake.getPoints().forEach(this::addSnakeSegment);
    }

    private void clearSnake() {
        removeAll(SnakeSegmentNode.class);
    }

    public void addSnakeSegment(Point point) {
        SnakeSegmentNode segmentNode;
        if (getNodes(SnakeSegmentNode.class).isEmpty()) {
            segmentNode = SnakeSegmentNode.head(gridSize);
        } else {
            segmentNode = SnakeSegmentNode.body(gridSize);
        }
        segmentNode.setPoint(point);
        segmentNode.setTranslateX(point.getX() * gridSize);
        segmentNode.setTranslateY(point.getY() * gridSize);
        snakePane.getChildren().add(segmentNode);
        gameNodes.add(segmentNode);
    }

    public void moveSnake(Snake snake, double duration) {
        for (int i = 0; i < snake.getPoints().size(); i++) {
            Node node = getNodes(SnakeSegmentNode.class).get(i);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(duration), node);
            tt.setInterpolator(Interpolator.LINEAR);
            Point point = snake.getPoints().get(i);
            tt.setToX(point.getX() * gridSize);
            tt.setToY(point.getY() * gridSize);
            tt.play();
        }
    }

}
