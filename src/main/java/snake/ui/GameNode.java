package snake.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import snake.model.Point;

class GameNode extends Rectangle {

    private final ObjectProperty<Point> point = new SimpleObjectProperty<>();

    GameNode(double size, Image image) {
        super(size, size);
        setFill(new ImagePattern(image));
    }

    public ReadOnlyObjectProperty<Point> pointProperty() {
        return point;
    }

    public Point getPoint() {
        return point.get();
    }

    public void setPoint(Point point) {
        this.point.set(point);
    }

}
