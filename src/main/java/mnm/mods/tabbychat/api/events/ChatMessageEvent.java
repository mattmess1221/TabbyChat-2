package mnm.mods.tabbychat.api.events;

import java.util.Set;

import com.google.common.collect.Sets;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.text.ITextComponent;

public abstract class ChatMessageEvent {

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
    public static class ChatReceivedEvent extends ChatMessageEvent {

        public ITextComponent text;
        public int id;
        public Set<Channel> channels = Sets.newHashSet();

        public ChatReceivedEvent(ITextComponent text, int id) {
            this.text = text;
            this.id = id;
        }
    }
}
