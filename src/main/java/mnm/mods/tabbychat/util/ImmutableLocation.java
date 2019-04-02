package mnm.mods.tabbychat.util;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class ImmutableLocation implements ILocation {

    private final int xPos;
    private final int yPos;
    private final int width;
    private final int height;

    public ImmutableLocation(int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getXPos() {
        return this.xPos;
    }

    @Override
    public int getYPos() {
        return this.yPos;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public String toString() {
        return "ImmutableLocation [xPos=" + xPos + ", yPos=" + yPos + ", width=" + width + ", height=" + height + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + height;
        result = prime * result + width;
        result = prime * result + xPos;
        result = prime * result + yPos;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ILocation))
            return false;
        ILocation other = (ILocation) obj;
        return height == other.getHeight()
                && width == other.getWidth()
                && xPos == other.getXPos()
                && yPos == other.getYPos();
    }

    @Override
    public ILocation asImmutable() {
        return this;
    }

}
