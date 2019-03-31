package mnm.mods.tabbychat.api.events;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public abstract class MessageAddedToChannelEvent extends Event {

    protected ITextComponent text;
    protected int id;
    private final Channel channel;

    protected MessageAddedToChannelEvent(ITextComponent text, int id, Channel channel) {
        this.text = text;
        this.id = id;
        this.channel = channel;
    }

    public ITextComponent getText() {
        return text;
    }

    public int getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    @Cancelable
    public static class Pre extends MessageAddedToChannelEvent {

        public Pre(ITextComponent text, int id, Channel channel) {
            super(text, id, channel);
        }

        public void setText(ITextComponent text) {
            this.text = text;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public static class Post extends MessageAddedToChannelEvent {

        public Post(ITextComponent text, int id, Channel channel) {
            super(text, id, channel);
        }
    }
}
