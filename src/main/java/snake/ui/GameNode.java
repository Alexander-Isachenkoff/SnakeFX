package snake.ui;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

class GameNode extends Rectangle {

    GameNode(double size, Image image) {
        super(size, size);
        setFill(new ImagePattern(image));
    }

}
