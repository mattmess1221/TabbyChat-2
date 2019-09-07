package mnm.mods.tabbychat.client;

public class UserChannel extends AbstractChannel {

    private String name;

    UserChannel(String name) {
        super(name);
        this.name = name;
        setPrefix("/msg " + name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDisplayName() {
        return toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
