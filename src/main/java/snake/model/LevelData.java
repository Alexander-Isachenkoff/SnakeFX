package snake.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import snake.FileUtils;

import javax.xml.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
public class LevelData {

    public static final String LEVELS_DIR = "data/levels/";
    @XmlElement(name = "obstacle")
    private final Set<Point> obstacles = new HashSet<>();
    @XmlAttribute
    private String name;
    @XmlAttribute
    @Setter
    private int bestScore;

    public LevelData(String name, Collection<Point> obstacles) {
        this.name = name;
        this.obstacles.addAll(obstacles);
    }

    public static void loadAllAsync(Consumer<LevelData> onLoad) {
        new Thread(() -> {
            try (Stream<Path> stream = Files.list(Paths.get(LEVELS_DIR))) {
                stream.map(path -> FileUtils.loadXmlObject(path.toString(), LevelData.class))
                        .forEach(onLoad);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void save() {
        FileUtils.saveXmlObject(this, LEVELS_DIR + getName() + ".xml");
    }

    public void delete() {
        try {
            Files.delete(Paths.get(LEVELS_DIR + getName() + ".xml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
