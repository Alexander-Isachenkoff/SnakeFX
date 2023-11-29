package snake.ui;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import snake.FileUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FoodNode extends Rectangle {

    private static List<Image> foodImages;

    public FoodNode(double size) {
        super(size, size);
        List<Image> images = getFoodImages();
        Image image = images.get(ThreadLocalRandom.current().nextInt(images.size()));
        setFill(new ImagePattern(image));
    }

    private List<Image> getFoodImages() {
        if (foodImages == null) {
            foodImages = FileUtils.extractImages("images/food/food.zip");
        }
        return foodImages;
    }

}
