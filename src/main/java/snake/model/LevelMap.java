package snake.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement
public class LevelMap {

    @XmlElement(name = "obstacle")
    private Set<Point> obstacles;

    public Set<Point> getObstacles() {
        return obstacles;
    }

}
