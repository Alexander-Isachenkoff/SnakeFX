package snake.ui;

import snake.FileUtils;

public class ObstacleNode extends GameNode {

    public ObstacleNode(double size) {
        super(size, FileUtils.loadImage("images/stone.png"));
    }

}
