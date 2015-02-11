package mnm.mods.tabbychat.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;

public class ChatChannel implements Channel {

    public static final Channel DEFAULT_CHANNEL = new ChatChannel("*", 0) {
        // Don't mess with this channel
        @Override
        public void setAlias(String alias) {}

        @Override
        public void setPrefix(String prefix) {}

        @Override
        public void setPrefixHidden(boolean hidden) {}

        @Override
        public void openSettings() {
            // There are no settings for this channel
            TabbyChat.getInstance().openSettings();
        }

        // Locked at 0
        @Override
        public void setPosition(int pos) {}
    };

    private List<Message> messages = Lists.newArrayList();

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

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addMessage(IChatComponent chat) {
        addMessage(chat, 0);
    }

    @Override
    public void addMessage(IChatComponent chat, int id) {
        Channel[] channels = TabbyChat.getInstance().getChat().getChannels();
        if (!Arrays.asList(channels).contains(this)) {
            TabbyChat.getInstance().getChat().addChannel(this);
        }
        if (id != 0) {
            removeMessages(id);
        }
        MessageAddedToChannelEvent event = new MessageAddedToChannelEvent(chat, id, this);
        TabbyChat.getInstance().getEventManager().onMessageAddedToChannel(event);
        if (event.chat == null) {
            return;
        }
        int uc = Minecraft.getMinecraft().ingameGUI.getUpdateCounter();
        Message msg = new ChatMessage(uc, event.chat, id);
        this.messages.add(0, msg);

    }

    @Override
    public void removeMessageAt(int pos) {
        this.messages.remove(pos);
    }

    @Override
    public void removeMessages(int id) {
        Iterator<Message> iter = this.messages.iterator();
        while (iter.hasNext()) {
            Message msg = iter.next();
            if (msg.getID() == id) {
                iter.remove();
            }
        }
    }

    @Override
    public void clear() {
        this.messages.clear();
    }

}
