package mnm.mods.tabbychat.api;

public interface Channel {

    String getName();

    int getPosition();

    void setPosition(int pos);

    String getAlias();

    void setAlias(String alias);

    String getPrefix();

    void setPrefix(String prefix);

    boolean isPrefixHidden();

    void setPrefixHidden(boolean hidden);

    boolean isActive();

    void setActive(boolean active);

    boolean isPending();

    void setPending(boolean pending);

    void openSettings();
}
