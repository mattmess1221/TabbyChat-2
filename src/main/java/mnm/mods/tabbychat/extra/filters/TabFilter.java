package mnm.mods.tabbychat.extra.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.api.filters.FilterEvent;

public abstract class TabFilter extends ChatFilter {

    public TabFilter(String action) {
        this.setAction(action);
    }

    @Override
    public void applyFilter(ChatReceivedEvent message) {
        String chat = message.chat.getUnformattedText();
        Pattern pattern = getPattern();
        if (pattern == null) return;
        Matcher matcher = pattern.matcher(chat);
        if (matcher.find()) {
            FilterEvent event = new FilterEvent(matcher, message.channels, message.chat);
            doAction(event);
            message.channels = event.channels;
        }
    }
}
