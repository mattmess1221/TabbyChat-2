package mnm.mods.tabbychat;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.listener.ChannelListener;
import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;

import com.google.common.collect.Maps;

public class ChatAddonAntiSpam implements ChannelListener {

    private Map<Channel, Counter> messageMap = Maps.newHashMap();

    @Override
    public void onMessageAdded(MessageAddedToChannelEvent event) {

        boolean prefEnableAntiSpam = TabbyChat.getInstance().generalSettings.antiSpam.getValue();
        boolean prefPartialMatching = TabbyChat.getInstance().generalSettings.antiSpamPartial.getValue();
        float   prefPartialMatchAmount = TabbyChat.getInstance().generalSettings.antiSpamPartialAmount.getValue();

        if (prefEnableAntiSpam && event.id == 0) {
            Channel channel = event.channel;
            Counter counter = this.messageMap.get(channel);
            if (counter == null) {
                counter = new Counter("");
                messageMap.put(channel, counter);
            }
            String chat = event.chat.getUnformattedText();
            if ((!prefPartialMatching && chat.equals(counter.lastMessage)) || (prefPartialMatching &&  getDifference(chat, counter.lastMessage) <= (prefPartialMatchAmount))) {
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

    private float getDifference(String s1, String s2){
        return StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase())*100/s1.length();
    }
}
