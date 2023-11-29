package snake;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import snake.model.LevelMap;
import snake.ui.LevelCell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LevelsMenuController {

    @FXML
    private FlowPane levelsPane;

    @FXML
    private void initialize() {
        try (Stream<Path> stream = Files.list(Paths.get("data/levels"))) {
            List<LevelCell> levelCells = stream
                    .map(path -> {
                        LevelMap levelMap = FileUtils.loadXmlObject(path.toString(), LevelMap.class);
                        String title = path.getFileName().toString().replace(".xml", "");
                        return new LevelCell(title, levelMap);
                    })
                    .collect(Collectors.toList());
            levelsPane.getChildren().setAll(levelCells);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
