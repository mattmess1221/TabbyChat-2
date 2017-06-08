package mnm.mods.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

public interface ILocation {

    int getXPos();

    int getYPos();

    int getWidth();

    int getHeight();

    ILocation asImmutable();

    default int getXWidth() {
        return getXPos() + getWidth();
    }

    default int getYHeight() {
        return getYPos() + getHeight();
    }

    default Location copy() {
        return Location.copyOf(this);
    }

    default Point getPoint() {
        return new Point(getXPos(), getYPos());
    }

    default Dimension getSize() {
        return new Dimension(this.getWidth(), this.getHeight());
    }

    default Rectangle asRectangle() {
        return new Rectangle(getXPos(), getYPos(), getWidth(), getHeight());
    }

    default boolean contains(ILocation r) {

        return this.getXPos() < r.getXWidth()
                && this.getXWidth() > r.getXPos()
                && this.getYPos() < r.getYHeight()
                && this.getYHeight() > r.getYPos();
    }
}
