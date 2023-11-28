package snake.model;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameModel {

    private final Snake snake = new Snake();
    private final Set<Point2D> foodSet = new HashSet<>();
    private Consumer<Snake> onSnakeMove;
    private Consumer<Point2D> onSegmentAdded;
    private Consumer<Point2D> onFoodAdded;
    private Consumer<Point2D> onFoodRemoved;
    private long lastUpdateTime;
    private long lastTurnTime;
    private long lastFoodTime;
    private double speed = 0.2;
    private int width = 40;
    private int height = 20;

    public Snake getSnake() {
        return snake;
    }

    public double getSpeed() {
        return speed;
    }

    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastUpdateTime == 0) {
                    lastUpdateTime = now;
                    lastTurnTime = now;
                }
                update(now);
            }
        }.start();
    }

    private void update(long now) {
        if (now - lastTurnTime > speed * 1e9) {
            lastTurnTime = now;
            snake.move();
            onSnakeMove.accept(snake);
            Set<Point2D> foodToEat = foodSet.stream()
                    .filter(food -> food.getX() == snake.getHead().getX())
                    .filter(food -> food.getY() == snake.getHead().getY())
                    .collect(Collectors.toSet());
            for (Point2D food : foodToEat) {
                Point2D segment = snake.addSegment();
                onSegmentAdded.accept(segment);
                foodSet.remove(food);
                onFoodRemoved.accept(food);
            }
        }
        if (now - lastFoodTime > 2 * 1e9) {
            lastFoodTime = now;
            spawnFood();
        }
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(width);
        int y = random.nextInt(height);
        Point2D food = new Point2D(x, y);
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
}