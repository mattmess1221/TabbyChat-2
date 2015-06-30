package mnm.mods.tabbychat;

import java.awt.Rectangle;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.settings.AdvancedSettings;
import mnm.mods.util.config.SettingMap;
import mnm.mods.util.gui.GuiText;

public class ChatManager implements Chat {

    private ChatBox chatbox;

    private Map<String, Channel> allChannels = Maps.newHashMap();
    private Map<String, Channel> allPms = Maps.newHashMap();
    private Set<Channel> channels = Sets.newHashSet(ChatChannel.DEFAULT_CHANNEL);
    private Channel active = ChatChannel.DEFAULT_CHANNEL;

    public ChatManager() {
        Rectangle rect = new Rectangle();

        AdvancedSettings settings = TabbyChat.getInstance().settings.advanced;
        rect.x = settings.chatX.getValue();
        rect.y = settings.chatY.getValue();
        rect.width = settings.chatW.getValue();
        rect.height = settings.chatH.getValue();

        this.chatbox = new ChatBox(rect);
    }

    @Override
    public Channel getChannel(String name) {
        return getChannel(name, false);
    }

    @Override
    public Channel getChannel(String name, boolean pm) {
        return pm ? getPmChannel(name) : getChatChannel(name);
    }

    private Channel getChatChannel(String name) {
        return getChannel(name, false, this.allChannels, TabbyChat.getInstance().serverSettings.channels);
    }

    private Channel getPmChannel(String name) {
        Channel channel = getChannel(name, true, this.allPms, TabbyChat.getInstance().serverSettings.pms);
        if (channel.getPrefix().isEmpty()) {
            channel.setPrefix("/msg " + name);
        }
        return channel;
    }

    private Channel getChannel(String name, boolean pm, Map<String, Channel> from, SettingMap<ChatChannel> setting) {
        if (!from.containsKey(name)) {
            // fetch from settings
            ChatChannel chan = setting.get(name);
            if (chan == null || chan.getName() == null) {
                chan = new ChatChannel(name, pm);
                setting.put(chan.getName(), chan);
                TabbyChat.getInstance().serverSettings.saveSettingsFile();
            }
            from.put(name, chan);
        }
        return from.get(name);
    }

    @Override
    public void addChannel(Channel channel) {
        if (!this.channels.contains(channel)) {
            this.channels.add(channel);
            chatbox.getTray().addChannel(channel);
        }
    }

    @Override
    public void removeChannel(Channel channel) {
        if (channels.contains(channel) && !channel.equals(ChatChannel.DEFAULT_CHANNEL)) {
            channels.remove(channel);
            chatbox.getTray().removeChannel(channel);
        }
        if (getActiveChannel() == channel) {
            setActiveChannel(ChatChannel.DEFAULT_CHANNEL);
        }
    }

    @Override
    public Channel[] getChannels() {
        return channels.toArray(new Channel[channels.size()]);
    }

    @Override
    public void removeMessages(int id) {
        for (Channel channel : this.channels) {
            channel.removeMessages(id);
        }
    }

    @Override
    public void clearMessages() {
        // TODO save messages somewhere
        for (Channel channel : channels) {
            channel.clear();
        }

        this.channels.clear();
        this.channels.add(ChatChannel.DEFAULT_CHANNEL);

        chatbox.getTray().clear();
    }

    @Override
    public Channel getActiveChannel() {
        return active;
    }

    @Override
    public void setActiveChannel(Channel channel) {
        GuiText text = chatbox.getChatInput().getTextField();
        if (active.isPrefixHidden()
                ? text.getValue().trim().isEmpty()
                : text.getValue().trim().equals(active.getPrefix())) {
            // text is the prefix, so remove it.
            text.setValue("");
            if (!channel.isPrefixHidden() && !channel.getPrefix().isEmpty()) {
                // target has prefix visible
                text.setValue(channel.getPrefix() + " ");
            }
        }
        // reset scroll
        // TODO per-channel scroll settings?
        if (channel != active) {
            chatbox.getChatArea().resetScroll();
        }
        active.setStatus(null);
        active = channel;
        active.setStatus(ChannelStatus.ACTIVE);
    }

    public ChatBox getChatBox() {
        return this.chatbox;
    }
}
