package mnm.mods.tabbychat.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import net.minecraft.util.StringUtils;

public class ChatFilter implements Filter {

    private FilterSettings settings = new ChatFilterSettings();
    private Pattern pattern;
    private IFilterAction action;

    @Override
    public void setPattern(String pattern) throws PatternSyntaxException {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public Pattern getPattern() {
        return this.pattern;
    }

    @Override
    public void setAction(IFilterAction action) {
        this.action = action;
    }

    @Override
    public IFilterAction getAction() {
        return action;
    }

    @Override
    public FilterSettings getSettings() {
        return settings;
    }

    public void applyFilter(ChatRecievedEvent message) {
        String chat = StringUtils.stripControlCodes(message.chat.getUnformattedText());

        // Iterate through matches
        Matcher matcher = getPattern().matcher(chat);
        while (matcher.find()) {
            FilterEvent event = new FilterEvent(matcher, message.chat);
            doAction(event);
            message.chat = event.chat; // Set the new chat
            message.channels.addAll(event.channels); // Add new channels.
        }
    }

    protected final void doAction(FilterEvent event) {
        if (getAction() != null) {
            getAction().action(this, event);
        }
    }
}
