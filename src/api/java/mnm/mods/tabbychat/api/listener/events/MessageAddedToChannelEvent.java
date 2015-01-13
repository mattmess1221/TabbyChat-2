package mnm.mods.tabbychat.api.listener.events;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.IChatComponent;

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
