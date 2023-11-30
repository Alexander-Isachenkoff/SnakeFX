package snake.ui;

import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import snake.FileUtils;

public class SnakeSegmentNode extends Circle {

    public SnakeSegmentNode(double size, boolean isHead) {
        super(size / 2, size / 2, size / 2);
        String path = isHead ? "images/snake/head.png" : "images/snake/body.png";
        setFill(new ImagePattern(FileUtils.loadImage(path)));
        Lighting lighting = new Lighting(new Light.Distant(235, 45, Color.WHITE));
        lighting.setDiffuseConstant(1.4);
        setEffect(lighting);
    }

}
