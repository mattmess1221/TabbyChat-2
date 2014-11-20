package mnm.mods.tabbychat.api.listener.events;

import java.util.List;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;

public abstract class ChatMessageEvent extends Event {

    /**
     * Used to listen to and change outbound chat.
     */
    public static class ChatSentEvent extends ChatMessageEvent {

        public final String message;

        public ChatSentEvent(String message) {
            this.message = message;
        }
    }

    /**
     * Filters the messages when they are sent.
     */
    public static class ChatSentFilterEvent extends ChatMessageEvent {

        public String message;

        public ChatSentFilterEvent(String message) {
            this.message = message;
        }
    }

    /**
     * Used to listen to chat and assign it a number of channels. Chat and
     * position cannot be changed. Use {@link ChatRecievedFilterEvent} instead.
     */
    public static class ChatRecievedEvent extends ChatMessageEvent {

        public final IChatComponent chat;
        public final int id;
        public final List<Channel> channels = Lists.newArrayList();

        public ChatRecievedEvent(IChatComponent chat, int id) {
            this.chat = chat;
            this.id = id;
        }
    }

    /**
     * Used to change or move chat.
     */
    public static class ChatRecievedFilterEvent extends ChatMessageEvent {

        public IChatComponent chat;
        public int id;

        public ChatRecievedFilterEvent(IChatComponent chat, int id) {
            this.chat = chat;
            this.id = id;
        }
    }
}
