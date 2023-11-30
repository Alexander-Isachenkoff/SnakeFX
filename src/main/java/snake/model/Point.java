package snake.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Point {

    @XmlAttribute
    private int x;
    @XmlAttribute
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
