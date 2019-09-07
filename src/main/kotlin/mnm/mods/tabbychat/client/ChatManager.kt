package mnm.mods.tabbychat.client;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mnm.mods.tabbychat.TCMarkers;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent;
import mnm.mods.tabbychat.client.gui.ChatBox;
import mnm.mods.tabbychat.client.settings.ServerSettings;
import mnm.mods.tabbychat.client.settings.TabbySettings;
import mnm.mods.tabbychat.util.ChannelTypeAdapter;
import mnm.mods.tabbychat.util.ChatTextUtils;
import mnm.mods.tabbychat.util.DateTimeTypeAdapter;
import mnm.mods.tabbychat.util.config.ValueMap;
import mnm.mods.tabbychat.util.text.TextBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ChatManager implements Chat {

    public static final int MAX_CHAT_LENGTH = 256;

    private static ChatManager instance;

    private Gson gson = new GsonBuilder()
            .registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer())
            .registerTypeAdapter(Style.class, new Style.Serializer())
            .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
            .registerTypeHierarchyAdapter(Channel.class, new ChannelTypeAdapter(this))
            .registerTypeAdapter(LocalDateTime.class, new DateTimeTypeAdapter())
            .create();

    private Map<String, Channel> allChannels = new HashMap<>();
    private Map<String, Channel> allPms = new HashMap<>();

    /**
     * A map of all the messages per channel
     */
    private Map<Channel, List<ChatMessage>> messages = new HashMap<>();

    /**
     * The split messages used for rendering the chatbox.
     */
    private Map<Channel, List<ChatMessage>> msgsplit = new HashMap<>();

    private ServerSettings server() {
        return TabbyChatClient.getInstance().getServerSettings();
    }

    private TabbySettings settings() {
        return TabbyChatClient.getInstance().getSettings();
    }

    private ChatManager() {
        allChannels.put("*", DefaultChannel.INSTANCE);
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
    public Channel getUserChannel(String name) {
        return allPms.computeIfAbsent(name, this.getChannel(name, server().pms, UserChannel::new));
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
                return Optional.of(getUserChannel(name));
            case '#':
                return Optional.of(getChannel(name));
            default:
                return Optional.empty();
        }

    }

    @Override
    public Set<Channel> getChannels() {
        return ImmutableSet.copyOf(ChatBox.getInstance().getChannels());
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
            removeMessages(channel, id);
        }

        int uc = Minecraft.getInstance().ingameGUI.getTicks();
        ChatMessage msg = new ChatMessage(uc, text, id, true);
        List<ChatMessage> messages = getChannelMessages(channel);
        messages.add(0, msg);

        trimMessages(messages, settings().advanced.historyLen.get());

        MinecraftForge.EVENT_BUS.post(new MessageAddedToChannelEvent.Post(text, id, channel));

        save();
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

    public void removeMessages(Channel chan, int id) {
        getChannelMessages(chan).removeIf(m -> m.getID() == id);
    }

    public void removeMessageAt(AbstractChannel channel, int index) {
        getChannelMessages(channel).remove(index);
        save();
    }

    public void clearMessages() {
        this.messages.clear();
        this.msgsplit.clear();

        this.allChannels.clear();
        this.allChannels.put("*", DefaultChannel.INSTANCE);
        this.allPms.clear();

        ChatBox.getInstance().clearMessages();
    }

    private static final Object lock = new Object();

    public void loadFrom(Path dir) throws IOException {
        synchronized (lock) {
            loadFrom_(dir);
        }
    }

    private static Reader newCompressedReader(Path path) throws IOException {
        return new InputStreamReader(new GzipCompressorInputStream(Files.newInputStream(path)), StandardCharsets.UTF_8);
    }

    private static Writer newCompressedWriter(Path path) throws IOException {
        return new OutputStreamWriter(new GzipCompressorOutputStream(Files.newOutputStream(path)), StandardCharsets.UTF_8);
    }

    private synchronized void loadFrom_(Path dir) throws IOException {
        Path file = dir.resolve("data.gz");
        if (Files.notExists(file)) {
            return;
        }

        clearMessages();

        try (Reader gzin = newCompressedReader(file)) {
            PersistantChat root = gson.fromJson(gzin, PersistantChat.class);

            this.messages.putAll(root.getMessages());
            ChatBox.getInstance().addChannels(root.getActive());

            ITextComponent chat = new TextBuilder()
                    // TODO use translation
                    .text("Chat log from " + root.getTime())
                    .format(TextFormatting.GRAY)
                    .build();

            for (Channel c : getChannels()) {
                if (!getMessages(c).isEmpty()) {
                    addMessage(c, chat, -1);
                }
            }
        }
    }

    public void save() {
        synchronized (lock) {
            try {
                saveTo(server().getPath().getParent());
            } catch (IOException e) {
                TabbyChat.logger.warn(TCMarkers.CHATBOX, "Error while saving chat data", e);
            }
        }
    }

    private synchronized void saveTo(Path dir) throws IOException {
        Path file = dir.resolve("data.gz");
        Files.createDirectories(dir);

        try (Writer gzout = newCompressedWriter(file)) {
            gson.toJson(new PersistantChat(messages, ChatBox.getInstance().getChannels()), PersistantChat.class, gzout);
        }
    }

    private static class PersistantChat {

        private LocalDateTime datetime;
        private Map<Channel, List<ChatMessage>> messages;
        private List<AbstractChannel> active;

        PersistantChat(Map<Channel, List<ChatMessage>> messages, Collection<AbstractChannel> active) {
            this.messages = new HashMap<>(messages);
            this.active = new ArrayList<>(active);
            this.datetime = LocalDateTime.now();
        }

        private String getTime() {
            return datetime == null ? "UNKNOWN" : datetime.toString();
        }

        private Map<Channel, List<ChatMessage>> getMessages() {
            return messages == null ? Collections.emptyMap() : messages;
        }

        private List<AbstractChannel> getActive() {
            return active == null ? Collections.emptyList() : active;
        }

    }
}
