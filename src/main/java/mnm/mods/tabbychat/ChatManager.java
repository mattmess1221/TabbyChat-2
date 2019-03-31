package mnm.mods.tabbychat;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.UserChannel;
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent;
import mnm.mods.tabbychat.settings.ServerSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.ChannelTypeAdapter;
import mnm.mods.tabbychat.util.ChatTextUtils;
import mnm.mods.tabbychat.util.DateTimeTypeAdapter;
import mnm.mods.util.config.ValueMap;
import mnm.mods.util.text.TextBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ChatManager implements Chat {

    public static final int MAX_CHAT_LENGTH = 256;

    private static ChatManager instance;

    public static final AbstractChannel DEFAULT_CHANNEL = new AbstractChannel("*") {
        @Override
        public String getName() {
            return getAlias();
        }

        @Override
        public String toString() {
            return "#" + getAlias();
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
    };

    private Gson gson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer())
            .registerTypeAdapter(Style.class, new Style.Serializer())
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
            .registerTypeAdapter(Channel.class, new ChannelTypeAdapter(this))
            .registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter())
            .create();

    private Map<String, Channel> allChannels = new HashMap<>();
    private Map<GameProfile, UserChannel> allPms = new HashMap<>();

    private Set<Channel> channels = new HashSet<>();
    private Map<Channel, List<ChatMessage>> messages = new HashMap<>();
    private Map<Channel, List<ChatMessage>> msgsplit = new HashMap<>();

    private ServerSettings server() {
        return TabbyChatClient.getInstance().getServerSettings();
    }

    private TabbySettings settings() {
        return TabbyChatClient.getInstance().getSettings();
    }

    private ChatManager() {
    }

    public static ChatManager instance() {
        if (instance == null) {
            instance = new ChatManager();
        }
        return instance;
    }

    @Override
    public Channel getChannel(String name) {
        return allChannels.computeIfAbsent(name, this.getChannel(name, server().channels, ChatChannel::new));
    }

    @Override
    public UserChannel getUserChannel(GameProfile gameProfile) {
        return allPms.computeIfAbsent(gameProfile, this.getChannel(gameProfile.getName(), server().pms, DirectChannel::new));
    }

    private <T, U> Function<U, T> getChannel(String name, ValueMap<T> config, Function<U, T> absent) {
        return profile -> config.get().computeIfAbsent(name, k -> absent.apply(profile));
    }

    /**
     * Parses a channel name and returns the channel. format must start with either # or @
     *
     * @param format The representation of the channel as a string
     * @return The channel
     */
    public Optional<Channel> parseChannel(String format) {
        if (format.length() <= 1) {
            return Optional.empty();
        }
        String name = format.substring(1);
        switch (format.charAt(0)) {
            case '@':
                return Optional.of(getUserChannel(new GameProfile(null, name)));
            case '#':
                return Optional.of(getChannel(name));
            default:
                throw new IllegalArgumentException();
        }

    }

    @Deprecated
    public void addChannel(AbstractChannel channel) {
        channels.add(channel);
    }

    @Deprecated
    public void removeChannel(AbstractChannel channel) {
        channels.remove(channel);
    }

    @Override
    public Set<Channel> getChannels() {
        // TODO only show visible channels.
        return ImmutableSet.copyOf(channels);
    }

    @Override
    public List<ChatMessage> getMessages(Channel channel) {
        return Collections.unmodifiableList(getChannelMessages(channel));
    }

    private List<ChatMessage> getChannelMessages(Channel channel) {
        return this.messages.computeIfAbsent(channel, k -> new ArrayList<>());
    }

    public List<ChatMessage> getVisible(Channel channel, int width) {
        return this.msgsplit.computeIfAbsent(channel, a -> ChatTextUtils.split(getMessages(a), width));
    }

    public void markDirty(Channel channel) {
        if (channel == null) {
            this.msgsplit.clear();
        } else {
            this.msgsplit.remove(channel);
        }
    }

    @Override
    public void addMessage(Channel channel, ITextComponent message) {
        addMessage(channel, message, 0);
    }

    public void addMessage(Channel channel, ITextComponent text, int id) {
        MessageAddedToChannelEvent event = new MessageAddedToChannelEvent.Pre(text.deepCopy(), id, channel);
        if (MinecraftForge.EVENT_BUS.post(event) || event.getText() == null) {
            return;
        }
        text = event.getText();
        id = event.getId();

        if (id != 0) {
            removeMessages(id);
        }

        int uc = Minecraft.getInstance().ingameGUI.getTicks();
        ChatMessage msg = new ChatMessage(uc, text, id, true);
        List<ChatMessage> messages = getChannelMessages(channel);
        messages.add(0, msg);

        trimMessages(messages, settings().advanced.historyLen.get());

        save();

        MinecraftForge.EVENT_BUS.post(new MessageAddedToChannelEvent.Post(text, id, channel));

        markDirty(channel);
    }

    private void trimMessages(List<?> list, int size) {
        Iterator<?> iter = list.iterator();

        for (int i = 0; iter.hasNext(); i++) {
            iter.next();
            if (i > size) {
                iter.remove();
            }
        }
    }

    public void removeMessages(int id) {
        for (List<ChatMessage> messages : this.messages.values()) {
            messages.removeIf(m -> m.getID() == id);
        }
        save();
    }

    public void removeMessageAt(AbstractChannel channel, int index) {
        getChannelMessages(channel).remove(index);
        save();
    }

    public void clearMessages() {
        this.messages.clear();
    }

    private static final Object lock = new Object();

    public void loadFrom(Path dir) throws IOException {
        synchronized (lock) {
            loadFrom_(dir);
        }
    }

    private synchronized void loadFrom_(Path dir) throws IOException {
        Path file = dir.resolve("data.gz");
        if (Files.notExists(file)) {
            return;
        }
        try (Reader gzin = new InputStreamReader(new GzipCompressorInputStream(Files.newInputStream(file)), StandardCharsets.UTF_8)) {
            JsonObject root = gson.fromJson(gzin, JsonObject.class);

            clearMessages();
            allChannels.clear();
            allPms.clear();

            Type type = new TypeToken<List<ChatMessage>>() {
            }.getType();

            List<ChatMessage> def = gson.fromJson(root.get("default"), type);
            getChannelMessages(DEFAULT_CHANNEL).addAll(def);

            readChannels(root.get("chans").getAsJsonObject());

            // active channels
            JsonObject active = root.get("active").getAsJsonObject();
            JsonArray achans = active.get("chans").getAsJsonArray();
            for (JsonElement e : achans) {
                addChannel(gson.fromJson(e, AbstractChannel.class));
            }

            String time;
            if (root.has("datetime")) {
                Instant datetime = Instant.ofEpochSecond(root.get("datetime").getAsLong());
                time = datetime.toString();
            } else {
                time = "UNKNOWN";
            }
            ITextComponent chat = new TextBuilder()
                    .text("Chat log from " + time)
                    .format(TextFormatting.GRAY)
                    .build();

            for (Channel c : getChannels()) {
                if (!getMessages(c).isEmpty()) {
                    addMessage(c, chat, -1);
                }
            }
        }
    }

    private void readChannels(JsonObject obj) {
        for (Entry<String, JsonElement> entry : obj.entrySet()) {
            Channel chan = getChannel(entry.getKey());
            List<ChatMessage> list = gson.fromJson(entry.getValue(), new TypeToken<List<ChatMessage>>(){}.getType());
            getChannelMessages(chan).addAll(list);
        }
    }

    private void readUserChannels(JsonObject obj) {
        for (Entry<String, JsonElement> entry : obj.entrySet()) {
            // profile is unsafe, but I'm not sent the uuid. Nothing I can do
            GameProfile profile = new GameProfile(null, entry.getKey());
            Channel chan = getUserChannel(profile);
            Type type = TypeUtils.parameterize(List.class, ChatMessage.class);
            List<ChatMessage> list = gson.fromJson(entry.getValue(), type);
            getChannelMessages(chan).addAll(list);
        }
    }

    public void save() {
        synchronized (lock) {
            try {
                saveTo(server().getPath().getParent());
            } catch (IOException e) {
                TabbyChat.logger.warn("Error while saving chat data", e);
            }
        }
    }

    private synchronized void saveTo(Path dir) throws IOException {
        JsonObject root = new JsonObject();
        root.addProperty("datetime", Instant.now().getEpochSecond());
        root.add("default", gson.toJsonTree(getMessages(DEFAULT_CHANNEL)));

        JsonObject chans = new JsonObject();
        root.add("chans", chans);

        for (Channel c : messages.keySet()) {
            chans.add(c.toString(), gson.toJsonTree(getMessages(c)));
        }

        // active channels
        JsonObject active = new JsonObject();
        root.add("active", active);

        JsonArray achans = new JsonArray();
        active.add("chans", achans);

        for (Channel c : channels) {
            if (c == DEFAULT_CHANNEL) {
                continue;
            }
            achans.add(gson.toJson(c));
        }

        Path file = dir.resolve("data.gz");
        Files.createDirectories(dir);
        try (Writer gzout = new OutputStreamWriter(new GzipCompressorOutputStream(Files.newOutputStream(file)), StandardCharsets.UTF_8)) {
            gson.toJson(root, gzout);
        }
    }
}
