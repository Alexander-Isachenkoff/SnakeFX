package snake.ui;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import snake.FileUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FoodNode extends Rectangle {

    public FoodNode(double size) {
        super(size, size);
        List<Image> foodImages = FileUtils.extractImages("images/food/food.zip");
        Image image = foodImages.get(ThreadLocalRandom.current().nextInt(foodImages.size()));
        setFill(new ImagePattern(image));
    }

}
