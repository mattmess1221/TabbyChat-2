package mnm.mods.tabbychat.api.events;

import com.google.common.collect.Sets;
import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;

public abstract class ChatMessageEvent extends Event {

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
