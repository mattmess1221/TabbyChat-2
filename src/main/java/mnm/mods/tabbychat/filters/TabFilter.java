package mnm.mods.tabbychat.filters;

import java.util.regex.Matcher;

import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;

public abstract class TabFilter extends ChatFilter implements IFilterAction {

    public TabFilter() {
        this.setAction(this);
    }

    @Override
    public void applyFilter(ChatRecievedEvent message) {
        String chat = message.chat.getUnformattedText();
        Matcher matcher = getPattern().matcher(chat);
        if (matcher.find()) {
            FilterEvent event = new FilterEvent(matcher, message.chat);
            doAction(event);
            message.channels.addAll(event.channels);
        }
    }
}
