package mnm.mods.tabbychat.api.events;

import java.util.Set;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Sets;

public abstract class ChatMessageEvent extends Event {

    /**
     * Used to listen to and change outbound chat.
     */
    public static class ChatSentEvent extends ChatMessageEvent {

        public String message;

        public ChatSentEvent(String message) {
            this.message = message;
        }
    }

    /**
     * Used to listen to chat and modify it. Can also select which channels it
     * goes to.
     */
    public static class ChatRecievedEvent extends ChatMessageEvent {

        public IChatComponent chat;
        public int id;
        public Set<Channel> channels = Sets.newHashSet();

        public ChatRecievedEvent(IChatComponent chat, int id) {
            this.chat = chat;
            this.id = id;
        }
    }
}
