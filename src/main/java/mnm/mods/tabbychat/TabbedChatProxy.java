package mnm.mods.tabbychat;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.util.IChatProxy;
import net.minecraft.util.IChatComponent;

public class TabbedChatProxy implements IChatProxy {

    @Override
    public void addToChat(String strchannel, IChatComponent msg) {
        Channel channel = TabbyAPI.getAPI().getChat().getChannel(strchannel);
        channel.addMessage(msg);
        channel.setStatus(ChannelStatus.UNREAD);
    }

}
