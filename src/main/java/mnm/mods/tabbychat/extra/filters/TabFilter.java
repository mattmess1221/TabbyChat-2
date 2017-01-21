package mnm.mods.tabbychat.extra.filters;

import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.api.filters.FilterEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TabFilter extends ChatFilter {

    TabFilter(String action) {
        this.setAction(action);
    }

    @Override
    public void applyFilter(ChatReceivedEvent message) {
        String chat = message.text.getUnformattedText();
        Pattern pattern = getPattern();
        if (pattern == null) return;
        Matcher matcher = pattern.matcher(chat);
        if (matcher.find()) {
            FilterEvent event = new FilterEvent(matcher, message.channels, message.text);
            doAction(event);
            message.channels = event.channels;
        }
    }
}
