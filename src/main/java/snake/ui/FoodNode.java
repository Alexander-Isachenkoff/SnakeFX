package snake.ui;

import javafx.scene.image.Image;
import snake.FileUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FoodNode extends GameNode {

    private static List<Image> foodImages;

    private FoodNode(double size, Image image) {
        super(size, image);
    }

    public static FoodNode random(double size) {
        List<Image> images = getFoodImages();
        Image image = images.get(ThreadLocalRandom.current().nextInt(images.size()));
        return new FoodNode(size, image);
    }

    private static List<Image> getFoodImages() {
        if (foodImages == null) {
            foodImages = FileUtils.extractImages("images/food/food.zip");
        }
        return foodImages;
    }

}
