package mnm.mods.tabbychat;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.settings.AdvancedSettings;
import mnm.mods.util.config.SettingMap;
import mnm.mods.util.gui.GuiText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.IChatComponent;

public class ChatManager implements Chat {

    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(IChatComponent.class, new IChatComponent.Serializer())
            .registerTypeAdapter(ChatStyle.class, new ChatStyle.Serializer())
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
            .create();

    private ChatBox chatbox;

    private Map<String, Channel> allChannels = Maps.newHashMap();
    private Map<String, Channel> allPms = Maps.newHashMap();
    private Set<Channel> channels = Sets.newHashSet(ChatChannel.DEFAULT_CHANNEL);
    private Channel active = ChatChannel.DEFAULT_CHANNEL;

    private Map<Channel, List<Message>> messages = Maps.newHashMap();

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
            messages.put(chan, chan.getMessages());
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

    void loadFrom(File dir) throws IOException {
        File file = new File(dir, "data.gz");
        if (!file.exists()) {
            return;
        }
        InputStream fin = null;
        InputStream gzin = null;
        String data;
        try {
            fin = new FileInputStream(file);
            gzin = new GzipCompressorInputStream(fin);
            data = IOUtils.toString(gzin, Charsets.UTF_8);
        } finally {
            IOUtils.closeQuietly(fin);
            IOUtils.closeQuietly(gzin);
        }

        clearMessages();
        allChannels.clear();
        allPms.clear();

        JsonObject root = gson.fromJson(data, JsonObject.class);
        Type type = TypeUtils.parameterize(List.class, ChatMessage.class);

        List<Message> def = gson.fromJson(root.get("default"), type);
        ChatChannel.DEFAULT_CHANNEL.getMessages().addAll(def);

        JsonObject chans = root.get("chans").getAsJsonObject();
        readJson(chans, false);
        JsonObject pms = root.get("pms").getAsJsonObject();
        readJson(pms, true);

        // active channels
        JsonObject active = root.get("active").getAsJsonObject();
        JsonArray achans = active.get("chans").getAsJsonArray();
        for (JsonElement e : achans) {
            addChannel(getChannel(e.getAsString(), false));
        }
        JsonArray apms = active.get("pms").getAsJsonArray();
        for (JsonElement e : apms) {
            addChannel(getChannel(e.getAsString(), true));
        }

    }

    private void readJson(JsonObject obj, boolean pm) {
        for (Entry<String, JsonElement> entry : obj.entrySet()) {
            Channel chan = getChannel(entry.getKey(), pm);
            Type type = TypeUtils.parameterize(List.class, ChatMessage.class);
            List<Message> list = gson.fromJson(entry.getValue(), type);
            chan.getMessages().addAll(list);
        }
    }

    void saveTo(File dir) throws IOException {
        JsonObject root = new JsonObject();
        root.add("default", gson.toJsonTree(ChatChannel.DEFAULT_CHANNEL.getMessages()));

        JsonObject chans = new JsonObject();
        root.add("chans", chans);
        JsonObject pms = new JsonObject();
        root.add("pms", pms);

        for (Entry<Channel, List<Message>> entry : messages.entrySet()) {
            Channel c = entry.getKey();
            JsonObject obj = c.isPm() ? pms : chans;
            obj.add(c.getName(), gson.toJsonTree(entry.getKey().getMessages()));
        }

        // active channels
        JsonObject active = new JsonObject();
        root.add("active", active);

        JsonArray apms = new JsonArray();
        JsonArray achans = new JsonArray();
        active.add("chans", achans);
        active.add("pms", apms);

        for (Channel c : channels) {
            if (c == ChatChannel.DEFAULT_CHANNEL) {
                continue;
            }
            JsonArray array = c.isPm() ? apms : achans;
            array.add(new JsonPrimitive(c.getName()));
        }

        OutputStream fout = null;
        GzipCompressorOutputStream gzout = null;
        try {
            File file = new File(dir, "data.gz");
            file.getParentFile().mkdirs();
            fout = new FileOutputStream(file);
            gzout = new GzipCompressorOutputStream(fout);
            String data = gson.toJson(root);
            IOUtils.write(data, gzout, Charsets.UTF_8);
            gzout.finish();
        } finally {
            IOUtils.closeQuietly(fout);
            IOUtils.closeQuietly(gzout);
        }
    }

    public ChatBox getChatBox() {
        return this.chatbox;
    }
}
