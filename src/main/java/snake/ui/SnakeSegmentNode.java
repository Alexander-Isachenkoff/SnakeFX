package snake.ui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class SnakeSegmentNode extends Circle {

    public SnakeSegmentNode(double size) {
        super(size / 2, size / 2, size / 2);
        setFill(Color.LIMEGREEN);
        setStroke(Color.GREEN);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(3);
    }

}
