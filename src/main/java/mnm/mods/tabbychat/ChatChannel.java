package mnm.mods.tabbychat;

import java.util.Objects;

public class ChatChannel extends AbstractChannel {

    private final String name;

    ChatChannel(String name) {
        super(name);
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "#" + name;
    }

}
