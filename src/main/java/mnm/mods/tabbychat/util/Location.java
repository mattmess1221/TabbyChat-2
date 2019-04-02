package mnm.mods.tabbychat.util;

public class Location implements ILocation {

    private int xPos;
    private int yPos;
    private int width;
    private int height;

    public Location() {
        this(0, 0, 1, 1);
    }

    public Location(int xPos, int yPos, int width, int height) {
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

    public Location setXPos(int xPos) {
        this.xPos = xPos;
        return this;
    }

    public Location setYPos(int yPos) {
        this.yPos = yPos;
        return this;
    }

    public Location setWidth(int width) {
        this.width = width;
        return this;
    }

    public Location setHeight(int height) {
        this.height = height;
        return this;
    }

    public Location move(int x, int y) {
        this.xPos += x;
        this.yPos += y;
        return this;
    }

    public Location scale(float scale) {
        this.xPos *= scale;
        this.yPos *= scale;
        this.width *= scale;
        this.height *= scale;
        return this;
    }

    @Override
    public String toString() {
        return "Location [xPos=" + xPos + ", yPos=" + yPos + ", width=" + width + ", height=" + height + "]";
    }

    @Override
    public int hashCode() {
        // only ImmutableLocation should be used in Maps
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
        if (obj == null || !(obj instanceof ILocation))
            return false;
        ILocation other = (ILocation) obj;
        return height == other.getHeight()
                && width == other.getWidth()
                && xPos == other.getXPos()
                && yPos == other.getYPos();
    }

    @Override
    public ILocation asImmutable() {
        return new ImmutableLocation(getXPos(), getYPos(), getWidth(), getHeight());
    }

    public static Location copyOf(ILocation location) {
        return new Location(location.getXPos(), location.getYPos(), location.getWidth(), location.getHeight());
    }
}
