package mnm.mods.tabbychat.client.extra;

import com.google.common.collect.Maps;
import mnm.mods.tabbychat.client.ChatChannel;
import mnm.mods.tabbychat.client.ChatManager;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ChatAddonAntiSpam {

    private final ChatManager chatManager;

    private Map<Channel, Counter> messageMap = Maps.newHashMap();

    public ChatAddonAntiSpam(ChatManager chatManager) {
        this.chatManager = chatManager;
    }

    @SubscribeEvent
    public void onMessageAdded(MessageAddedToChannelEvent.Pre event) {

        boolean enabled = TabbyChatClient.getInstance().getSettings().general.antiSpam.get();
        double prejudice = TabbyChatClient.getInstance().getSettings().general.antiSpamPrejudice.get();

        if (enabled && event.getId() == 0) {
            ChatChannel channel = (ChatChannel) event.getChannel();
            Counter counter = this.messageMap.computeIfAbsent(channel, k -> new Counter());
            String chat = event.getText().getString();

            if (getDifference(chat, counter.lastMessage) <= prejudice) {
                counter.spamCounter++;
                event.getText().appendText(" [" + counter.spamCounter + "x]");
                chatManager.removeMessageAt(channel, 0);
                chatManager.markDirty(channel);
            } else {
                counter.lastMessage = chat;
                counter.spamCounter = 1;
            }
        }
    }

    private class Counter {
        private String lastMessage = "";
        private int spamCounter = 1;
    }

    private static double getDifference(String s1, String s2) {
        double avgLen = (s1.length() + s2.length()) / 2D;
        if (avgLen == 0) {
            return 0;
        }
        return StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase()) / avgLen;
    }
}
