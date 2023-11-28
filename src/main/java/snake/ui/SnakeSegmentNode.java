package snake.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class SnakeSegmentNode extends Circle {

    public SnakeSegmentNode(double size) {
        super(size / 2, size / 2, size / 2);
        setFill(Color.LIMEGREEN);
        setStroke(Color.GREEN);
        setStrokeWidth(3);
    }

}
