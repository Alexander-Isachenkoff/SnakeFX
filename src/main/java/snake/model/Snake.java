package snake.model;

import javafx.geometry.Point2D;
import javafx.geometry.Side;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private final List<Point2D> points = new ArrayList<>();
    private Side direction = Side.TOP;

    Snake() {
        points.add(new Point2D(20, 20));
        points.add(new Point2D(20, 21));
        points.add(new Point2D(20, 22));
        points.add(new Point2D(20, 23));
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public void turnUp() {
        direction = Side.TOP;
    }

    public void turnDown() {
        direction = Side.BOTTOM;
    }

    public void turnLeft() {
        direction = Side.LEFT;
    }

    public void turnRight() {
        direction = Side.RIGHT;
    }

    void move() {
        switch (direction) {
            case TOP:
                moveUp();
                break;
            case BOTTOM:
                moveDown();
                break;
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
        }
    }

    private void moveUp() {
        move(0, -1);
    }

    private void moveDown() {
        move(0, 1);
    }

    private void moveLeft() {
        move(-1, 0);
    }

    private void moveRight() {
        move(1, 0);
    }

    private void move(double x, double y) {
        points.remove(points.size() - 1);
        Point2D head = getHead();
        points.add(0, new Point2D(head.getX() + x, head.getY() + y));
    }

    Point2D addSegment() {
        Point2D last = points.get(points.size() - 1);
        Point2D preLast = points.get(points.size() - 2);
        int x = 0, y = 0;
        if (last.getX() == preLast.getX()) {
            x = (int) last.getX();
            y = (int) (last.getY() + last.getY() - preLast.getY());
        }
        if (last.getY() == preLast.getY()) {
            y = (int) last.getY();
            x = (int) (last.getX() + last.getX() - preLast.getX());
        }
        Point2D newSegment = new Point2D(x, y);
        points.add(newSegment);
        return newSegment;
    }

    Point2D getHead() {
        return points.get(0);
    }

}
