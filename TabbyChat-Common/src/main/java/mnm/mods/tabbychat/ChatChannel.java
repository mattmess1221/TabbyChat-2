package mnm.mods.tabbychat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;
import mnm.mods.tabbychat.gui.ChatArea;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.gui.settings.GuiSettingsChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;

public class ChatChannel implements Channel {

    public static final Channel DEFAULT_CHANNEL = new ChatChannel("*") {
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
            TabbyChat.getInstance().openSettings(null);
        }
    };

    private transient List<Message> messages = Lists.newArrayList();

    private final String name;
    private String alias;

    private String prefix = "";
    private boolean prefixHidden = false;

    private transient boolean active = false;
    private transient boolean pending = false;

    public ChatChannel(String name) {
        this.name = name;
        this.alias = this.name;
    }

    @Override
    public String getName() {
        return this.name;
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
        if (!pending || !isActive()) {
            this.pending = pending;
        }
    }

    @Override
    public void openSettings() {
        TabbyChat.getInstance().openSettings(new GuiSettingsChannel(this));
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
        Message msg = new ChatMessage(uc, event.chat, id, true);
        this.messages.add(0, msg);

        // compensate scrolling
        ChatArea chatbox = ((ChatBox) TabbyChat.getInstance().getChat()).getChatArea();
        if (active && chatbox.getScrollPos() > 0 && id == 0) {
            chatbox.scroll(1);
        }

        trim(TabbyChat.getInstance().advancedSettings.historyLen.getValue());

    }

    public void trim(int size) {
        Iterator<Message> iter = this.messages.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            iter.next();
            if (i > size) {
                iter.remove();
            }
        }
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

    public static class ChannelMap extends HashMap<String, Channel> {
        private static final long serialVersionUID = 5235330277084637079L;
    }
}
