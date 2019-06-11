package mnm.mods.tabbychat.util;

public class Vec2i {

    public final int x;
    public final int y;

    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        if (obj.getClass() == Vec2i.class) {
            Vec2i vec = (Vec2i) obj;
            return this.x == vec.x && this.y == vec.y;
        }
        return false;
    }
}
