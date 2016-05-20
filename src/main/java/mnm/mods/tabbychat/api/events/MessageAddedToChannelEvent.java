package mnm.mods.tabbychat.api.events;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.text.ITextComponent;

public class MessageAddedToChannelEvent {

    public ITextComponent text;
    public int id;
    public final Channel channel;

    public MessageAddedToChannelEvent(ITextComponent text, int id, Channel channel) {
        this.text = text;
        this.id = id;
        this.channel = channel;
    }
}
