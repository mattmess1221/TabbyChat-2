package mnm.mods.tabbychat;

public final class DefaultChannel extends ChatChannel {

    public static final AbstractChannel INSTANCE = new DefaultChannel();

    private DefaultChannel() {
        super("*");
    }

    @Override
    public String getDisplayName() {
        return getName();
    }

    // Don't mess with this channel
    @Override
    public void setAlias(String alias) {
    }

    @Override
    public void setPrefix(String prefix) {
    }

    @Override
    public void setPrefixHidden(boolean hidden) {
    }

    @Override
    public void setCommand(String command) {
    }
}
