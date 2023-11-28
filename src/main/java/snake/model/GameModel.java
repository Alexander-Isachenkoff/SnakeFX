package snake.model;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameModel {

    private Snake snake;
    private final Set<Point2D> foodSet = new HashSet<>();
    private final Random random = new Random();
    private final IntegerProperty score = new SimpleIntegerProperty(-1);
    private final IntegerProperty record = new SimpleIntegerProperty(-1);
    private final AnimationTimer timer;
    private Consumer<Snake> onSnakeMove;
    private Consumer<Point2D> onSegmentAdded;
    private Consumer<Point2D> onFoodAdded;
    private Consumer<Point2D> onFoodRemoved;
    private Runnable onGameOver;
    private long lastUpdateTime;
    private long lastTurnTime;
    private long lastFoodTime;
    private final double speed = 0.2;
    private final int width = 32;
    private final int height = 24;

    public GameModel() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    lastTurnTime = now;
                }
                update(now);
            }
        };
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Snake getSnake() {
        return snake;
    }

    public double getSpeed() {
        return speed;
    }

    public void restart() {
        for (Point2D food : new HashSet<>(foodSet)) {
            removeFood(food);
        }
        snake = new Snake();
        score.set(0);
        lastUpdateTime = 0;
        lastTurnTime = 0;
        lastFoodTime = 0;
        timer.start();
    }

    private void update(long now) {
        if (now - lastTurnTime > speed * 1e9) {
            lastTurnTime = now;
            snake.move();

            Point2D head = snake.getHead();

            boolean intersects = snake.getPoints().stream()
                    .filter(point -> point != head)
                    .filter(point -> point.getX() == head.getX())
                    .anyMatch(point -> point.getY() == head.getY());
            if (intersects) {
                timer.stop();
                onGameOver.run();
                return;
            }

            if (head.getX() < 0 || head.getX() >= width || head.getY() < 0 || head.getY() >= height) {
                timer.stop();
                onGameOver.run();
                return;
            }

            onSnakeMove.accept(snake);

            Set<Point2D> foodToEat = foodSet.stream()
                    .filter(food -> food.getX() == head.getX())
                    .filter(food -> food.getY() == head.getY())
                    .collect(Collectors.toSet());
            for (Point2D food : foodToEat) {
                Point2D segment = snake.addSegment();
                onSegmentAdded.accept(segment);
                removeFood(food);
                score.set(score.get() + 1);
            }
        }
        if (now - lastFoodTime > 2 * 1e9) {
            lastFoodTime = now;
            spawnFood();
        }
    }

    private void removeFood(Point2D food) {
        foodSet.remove(food);
        onFoodRemoved.accept(food);
    }

    private void spawnFood() {
        List<Point2D> availablePoints = new ArrayList<>();
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= height; j++) {
                int x = i;
                int y = j;
                if (foodSet.stream().noneMatch(point2D -> point2D.getX() == x && point2D.getY() == y)) {
                    if (snake.getPoints().stream().noneMatch(point2D -> point2D.getX() == x && point2D.getY() == y)) {
                        availablePoints.add(new Point2D(x, y));
                    }
                }
            }
        }
        Point2D food = availablePoints.get(random.nextInt(availablePoints.size()));
        foodSet.add(food);
        onFoodAdded.accept(food);
    }

    public void setOnSnakeMove(Consumer<Snake> onSnakeMove) {
        this.onSnakeMove = onSnakeMove;
    }

    public void setOnFoodAdded(Consumer<Point2D> onFoodAdded) {
        this.onFoodAdded = onFoodAdded;
    }

    public void setOnFoodRemoved(Consumer<Point2D> onFoodRemoved) {
        this.onFoodRemoved = onFoodRemoved;
    }

    public void setOnSegmentAdded(Consumer<Point2D> onSegmentAdded) {
        this.onSegmentAdded = onSegmentAdded;
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public ReadOnlyIntegerProperty scoreProperty() {
        return score;
    }

    public ReadOnlyIntegerProperty recordProperty() {
        return record;
    }

}
