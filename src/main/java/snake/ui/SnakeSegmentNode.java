package snake.ui;

import javafx.scene.image.Image;
import snake.FileUtils;

public class SnakeSegmentNode extends GameNode {

    public SnakeSegmentNode(double size, Image image) {
        super(size, image);
    }

    public static SnakeSegmentNode body(double size) {
        return new SnakeSegmentNode(size, FileUtils.loadImage("images/snake/body.png"));
    }

    public static SnakeSegmentNode head(double size) {
        return new SnakeSegmentNode(size, FileUtils.loadImage("images/snake/head.png"));
    }

}
