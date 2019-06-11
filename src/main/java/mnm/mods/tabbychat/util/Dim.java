package mnm.mods.tabbychat.util;

public class Dim {

    public final int width;
    public final int height;

    public Dim(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean equals(Object obj) {
        if (obj.getClass() == Dim.class) {
            Dim dim = (Dim) obj;
            return this.width == dim.width && this.height == dim.height;
        }
        return false;
    }
}
