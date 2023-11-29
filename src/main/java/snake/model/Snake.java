package snake.model;

import javafx.geometry.Side;

import java.util.ArrayList;
import java.util.List;

public class Snake {

    private final List<Point> points = new ArrayList<>();
    private Side direction = Side.TOP;

    Snake() {
        points.add(new Point(20, 20));
        points.add(new Point(20, 21));
        points.add(new Point(20, 22));
        points.add(new Point(20, 23));
    }

    public List<Point> getPoints() {
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

    private void move(int x, int y) {
        points.remove(points.size() - 1);
        Point head = getHead();
        points.add(0, new Point(head.getX() + x, head.getY() + y));
    }

    Point addSegment() {
        Point last = points.get(points.size() - 1);
        Point preLast = points.get(points.size() - 2);
        int x = 0, y = 0;
        if (last.getX() == preLast.getX()) {
            x = last.getX();
            y = last.getY() + last.getY() - preLast.getY();
        }
        if (last.getY() == preLast.getY()) {
            y = last.getY();
            x = last.getX() + last.getX() - preLast.getX();
        }
        Point newSegment = new Point(x, y);
        points.add(newSegment);
        return newSegment;
    }

    Point getHead() {
        return points.get(0);
    }

    boolean intersectsSelf() {
        Point head = getHead();
        return getPoints().stream()
                .filter(point -> point != head)
                .filter(point -> point.getX() == head.getX())
                .anyMatch(point -> point.getY() == head.getY());
    }

}
