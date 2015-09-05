package mnm.mods.tabbychat.api.events;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.IChatComponent;

/**
 * Called whenever a message is added to a channel. To cancel, subscribe to
 * {@link ChatMessageEvent} and change the destination channels instead.
 */
public class MessageAddedToChannelEvent extends Event {

    public IChatComponent chat;
    public int id;
    public final Channel channel;

    public MessageAddedToChannelEvent(IChatComponent chat, int id, Channel channel) {
        this.chat = chat;
        this.id = id;
        this.channel = channel;
    }
}
