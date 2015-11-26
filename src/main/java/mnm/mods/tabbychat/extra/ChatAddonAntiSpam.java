package mnm.mods.tabbychat.extra;

import java.util.Map;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.listener.ChannelListener;
import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Maps;

public class ChatAddonAntiSpam implements ChannelListener {

    private Map<Channel, Counter> messageMap = Maps.newHashMap();

    @Override
    public void onMessageAdded(MessageAddedToChannelEvent event) {

        boolean enabled = TabbyChat.getInstance().settings.general.antiSpam.get();
        double prejudice = TabbyChat.getInstance().settings.general.antiSpamPrejudice.get();

        if (enabled && event.id == 0) {
            Channel channel = event.channel;
            Counter counter = this.messageMap.get(channel);
            if (counter == null) {
                counter = new Counter("");
                messageMap.put(channel, counter);
            }
            String chat = event.chat.getUnformattedText();

            if (getDifference(chat, counter.lastMessage) <= prejudice) {
                counter.spamCounter++;
                event.chat.appendText(" [" + counter.spamCounter + "x]");
                channel.removeMessageAt(0);
            } else {
                counter.lastMessage = chat;
                counter.spamCounter = 1;
            }
        }
    }

    private class Counter {
        private String lastMessage;
        private int spamCounter = 1;

        private Counter(String lastMessage) {
            this.lastMessage = lastMessage;
        }
    }

    private static double getDifference(String s1, String s2) {
        double avgLen = (s1.length() + s2.length()) / 2D;
        if (avgLen == 0) {
            return 0;
        }
        return StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase()) / avgLen;
    }
}
