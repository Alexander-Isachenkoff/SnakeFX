package snake.model;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GameModel {

    private final Set<Point> foodSet = new HashSet<>();
    private final Random random = new Random();
    private final IntegerProperty score = new SimpleIntegerProperty(-1);
    private final IntegerProperty bestScore = new SimpleIntegerProperty(-1);
    private final AnimationTimer timer;
    private final double speed = 0.2;
    private final int width = 32;
    private final int height = 24;
    private final Set<Point> obstacles = new HashSet<>();
    private Snake snake;
    private Consumer<Snake> onSnakeMove;
    private Consumer<Point> onSegmentAdded;
    private Consumer<Point> onFoodAdded;
    private Consumer<Point> onFoodRemoved;
    private Runnable onGameOver;
    private long lastUpdateTime;
    private long lastTurnTime;
    private long lastFoodTime;
    private LevelData levelData;

    public GameModel() {
        score.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > bestScore.get()) {
                bestScore.set(newValue.intValue());
                levelData.setBestScore(bestScore.get());
                levelData.save();
            }
        });
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

    public void init(LevelData levelData) {
        this.levelData = levelData;
        bestScore.set(levelData.getBestScore());
        obstacles.clear();
        obstacles.addAll(levelData.getObstacles());
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
        for (Point food : new HashSet<>(foodSet)) {
            removeFood(food);
        }
        snake = new Snake();
        score.set(0);
        lastUpdateTime = 0;
        lastTurnTime = 0;
        lastFoodTime = 0;
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void update(long now) {
        if (now - lastTurnTime > speed * 1e9) {
            lastTurnTime = now;
            snake.move();

            Point head = snake.getHead();

            if (snake.intersectsSelf()) {
                timer.stop();
                onGameOver.run();
                return;
            }

            boolean intersectsObstacle = obstacles.stream()
                    .filter(point -> point != head)
                    .filter(point -> point.getX() == head.getX())
                    .anyMatch(point -> point.getY() == head.getY());
            if (intersectsObstacle) {
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

            Set<Point> foodToEat = foodSet.stream()
                    .filter(food -> food.getX() == head.getX())
                    .filter(food -> food.getY() == head.getY())
                    .collect(Collectors.toSet());
            for (Point food : foodToEat) {
                Point segment = snake.addSegment();
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

    private void removeFood(Point food) {
        foodSet.remove(food);
        onFoodRemoved.accept(food);
    }

    private void spawnFood() {
        List<Point> availablePoints = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Set<Point> objects = new HashSet<>();
                objects.addAll(foodSet);
                objects.addAll(snake.getPoints());
                objects.addAll(obstacles);
                Point point = new Point(x, y);
                if (!objects.contains(point)) {
                    availablePoints.add(point);
                }
            }
        }
        Point food = availablePoints.get(random.nextInt(availablePoints.size()));
        foodSet.add(food);
        onFoodAdded.accept(food);
    }

    public void setOnSnakeMove(Consumer<Snake> onSnakeMove) {
        this.onSnakeMove = onSnakeMove;
    }

    public void setOnFoodAdded(Consumer<Point> onFoodAdded) {
        this.onFoodAdded = onFoodAdded;
    }

    public void setOnFoodRemoved(Consumer<Point> onFoodRemoved) {
        this.onFoodRemoved = onFoodRemoved;
    }

    public void setOnSegmentAdded(Consumer<Point> onSegmentAdded) {
        this.onSegmentAdded = onSegmentAdded;
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public ReadOnlyIntegerProperty scoreProperty() {
        return score;
    }

    public ReadOnlyIntegerProperty bestScoreProperty() {
        return bestScore;
    }

}
