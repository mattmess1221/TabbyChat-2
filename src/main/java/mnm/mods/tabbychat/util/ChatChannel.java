package mnm.mods.tabbychat.util;

import mnm.mods.tabbychat.api.Channel;

public class ChatChannel implements Channel {

    public static final Channel DEFAULT_CHANNEL = new ChatChannel("*", 0) {
        // Don't mess with this channel
        @Override
        public void setAlias(String alias) {
        };

        @Override
        public void setPrefix(String prefix) {
        };

        @Override
        public void setPrefixHidden(boolean hidden) {
        };

        @Override
        public void openSettings() {
            // There are no settings for this channel
            // TabbyChat.getInstance().openSettings();
        };

        // Locked at 0
        @Override
        public void setPosition(int pos) {
        };
    };

    private final String name;
    private String alias;

    private String prefix = "";
    private boolean prefixHidden = false;

    private boolean active = false;
    private boolean pending = false;

    private int position;

    public ChatChannel(String name, int pos) {
        this.name = name;
        this.alias = this.name;
        this.position = pos;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(int pos) {
        this.position = pos;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean isPrefixHidden() {
        return this.prefixHidden;
    }

    @Override
    public void setPrefixHidden(boolean hidden) {
        this.prefixHidden = hidden;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void setActive(boolean selected) {
        this.active = selected;
        setPending(false);
    }

    @Override
    public boolean isPending() {
        return this.pending;
    }

    @Override
    public void setPending(boolean pending) {
        this.pending = pending;
    }

    @Override
    public void openSettings() {
        // TODO Auto-generated method stub

    }

}
