package mnm.mods.tabbychat.util;

import java.util.List;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Message;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;

public class ChatMessage extends ChatLine implements Message {

    private List<Channel> channels = Lists.newArrayList();

    public ChatMessage(int updatedCounter, IChatComponent chat, int id) {
        super(updatedCounter, chat, id);
        this.channels.add(new ChatChannel("Derp", 0));
    }

    public ChatMessage(ChatLine chatline) {
        this(chatline.getUpdatedCounter(), chatline.getChatComponent(), chatline.getChatLineID());
    }

    @Override
    public void addChannel(Channel... channels) {
        for (Channel chan : channels) {
            if (!this.channels.contains(chan)) {
                this.channels.add(chan);
            }
        }
    }

    @Override
    public Channel[] getChannels() {
        return channels.toArray(new Channel[0]);
    }

    @Override
    public IChatComponent getMessage() {
        return this.getChatComponent();
    }

    @Override
    public int getCounter() {
        return this.getUpdatedCounter();
    }

    @Override
    public int getID() {
        return this.getChatLineID();
    }

    @Override
    public boolean isActive() {
        boolean result = false;
        Channel[] channels = getChannels();
        int i = 0;
        while (i < channels.length && !result) {
            result = channels[i].isActive();
            i++;
        }
        return result;
    }
}
