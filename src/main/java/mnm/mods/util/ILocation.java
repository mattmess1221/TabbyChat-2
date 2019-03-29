package mnm.mods.util;

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

    default int getXCenter() {
        return getXPos() + getWidth() / 2;
    }

    default int getYCenter() {
        return getYPos() + getHeight() / 2;
    }

    default Location copy() {
        return Location.copyOf(this);
    }

    default Vec getPoint() {
        return new Vec(getXPos(), getYPos());
    }

    default Dim getSize() {
        return new Dim(this.getWidth(), this.getHeight());
    }

    default boolean contains(ILocation r) {
        return this.getXPos() < r.getXWidth()
                && this.getXWidth() > r.getXPos()
                && this.getYPos() < r.getYHeight()
                && this.getYHeight() > r.getYPos();
    }

    default boolean contains(double x, double y) {
        return this.getXPos() < x
                && this.getYPos() < y
                && this.getXWidth() > x
                && this.getYHeight() > y;
    }
}
