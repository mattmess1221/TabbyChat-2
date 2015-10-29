package mnm.mods.tabbychat;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.util.IChatProxy;
import net.minecraft.util.ChatComponentText;

public class TabbedChatProxy implements IChatProxy {

    @Override
    public void addToChat(String strchannel, String msg) {
        Channel channel = TabbyAPI.getAPI().getChat().getChannel(strchannel);
        channel.addMessage(new ChatComponentText(msg));
        channel.setStatus(ChannelStatus.UNREAD);
    }

}
