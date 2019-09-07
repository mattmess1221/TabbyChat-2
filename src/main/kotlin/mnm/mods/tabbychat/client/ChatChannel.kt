package mnm.mods.tabbychat.client;

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
    public String getDisplayName() {
        return "#" + getAlias();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "#" + name;
    }

}
