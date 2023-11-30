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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@NoArgsConstructor
public class LevelData {

    public static final String LEVELS_DIR = "data/levels/";

    @XmlAttribute
    private String name;
    @XmlAttribute
    @Setter
    private int bestScore;
    @XmlElement(name = "obstacle")
    private final Set<Point> obstacles = new HashSet<>();

    public LevelData(String name, Collection<Point> obstacles) {
        this.name = name;
        this.obstacles.addAll(obstacles);
    }

    public static List<LevelData> load() {
        try (Stream<Path> stream = Files.list(Paths.get(LEVELS_DIR))) {
            return stream
                    .map(path -> FileUtils.loadXmlObject(path.toString(), LevelData.class))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        FileUtils.saveXmlObject(this, LEVELS_DIR + getName() + ".xml");
    }

}
