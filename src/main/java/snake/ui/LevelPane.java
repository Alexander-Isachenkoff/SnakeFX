package snake.ui;

import javafx.scene.layout.*;
import snake.FileUtils;
import snake.model.Point;

import java.util.Set;

public class LevelPane extends Pane {

    private final double gridSize;

    public LevelPane(double gridSize, int width, int height) {
        this.gridSize = gridSize;
        this.setPrefWidth(gridSize * width);
        this.setPrefHeight(gridSize * height);
        this.setBackground(new Background(new BackgroundImage(FileUtils.loadImage("images/terrain.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
    }

    public void setObstacles(Set<Point> obstacles) {
        for (Point obstacle : obstacles) {
            ObstacleNode node = new ObstacleNode(gridSize);
            node.setTranslateX(obstacle.getX() * gridSize);
            node.setTranslateY(obstacle.getY() * gridSize);
            getChildren().add(node);
        }
    }

}
