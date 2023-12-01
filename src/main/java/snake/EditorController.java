package snake;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import snake.model.LevelData;
import snake.model.Point;
import snake.model.Snake;
import snake.ui.LevelPane;

public class EditorController {

    private final double GRID_SIZE = 20;
    private final LevelPane levelPane = new LevelPane(GRID_SIZE, 32, 24);
    @FXML
    private VBox levelPaneWrapper;
    @FXML
    private TextField nameField;
    private final Rectangle selector = new Rectangle();

    @FXML
    private void initialize() {
        levelPaneWrapper.getChildren().setAll(levelPane);
        AnchorPane.setLeftAnchor(levelPane, 0.0);
        AnchorPane.setRightAnchor(levelPane, 0.0);
        AnchorPane.setTopAnchor(levelPane, 0.0);
        AnchorPane.setBottomAnchor(levelPane, 0.0);

        levelPane.getChildren().add(selector);
        selector.setArcHeight(10);
        selector.setArcWidth(10);
        selector.setFill(Color.TRANSPARENT);
        selector.setStroke(Color.WHITE);
        selector.setStrokeWidth(2);
        selector.setWidth(GRID_SIZE);
        selector.setHeight(GRID_SIZE);

        levelPane.setOnMouseMoved(this::moveSelector);
        levelPane.setOnMousePressed(this::onMouseEvent);
        levelPane.setOnMouseDragged(this::onMouseEvent);
        levelPane.getChildren().add(new Pane(selector));

        levelPane.initSnake(new Snake());
    }

    private void moveSelector(MouseEvent event) {
        if (isInBounds(event, levelPane)) {
            double x = Math.floor(event.getX() / GRID_SIZE) * GRID_SIZE;
            double y = Math.floor(event.getY() / GRID_SIZE) * GRID_SIZE;
            selector.setTranslateX(x);
            selector.setTranslateY(y);
        }
    }

    private void onMouseEvent(MouseEvent event) {
        if (!isInBounds(event, levelPane)) {
            return;
        }
        moveSelector(event);
        int xIndex = (int) Math.floor(event.getX() / GRID_SIZE);
        int yIndex = (int) Math.floor(event.getY() / GRID_SIZE);
        Point point = new Point(xIndex, yIndex);
        if (event.getButton() == MouseButton.PRIMARY) {
            if (!levelPane.hasObstacle(point) && !levelPane.hasSnakeSegment(point)) {
                levelPane.addObstacle(point);
            }
        }
        if (event.getButton() == MouseButton.SECONDARY) {
            levelPane.removeObstacle(point);
        }
    }

    private boolean isInBounds(MouseEvent event, Region node) {
        return event.getX() >= 0 && event.getY() >= 0 && event.getX() < node.getWidth() && event.getY() < node.getHeight();
    }

    @FXML
    private void onMenu() {
        Main.toMainMenu();
    }

    @FXML
    private void onSave() {
        LevelData levelData = new LevelData(nameField.getText(), levelPane.getObstacles());
        levelData.save();
    }

    @FXML
    private void onClear() {
        levelPane.clearObstacles();
    }

}
