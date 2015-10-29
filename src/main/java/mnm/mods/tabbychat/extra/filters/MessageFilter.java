package mnm.mods.tabbychat.extra.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.util.MessagePatterns;

/**
 * Base class for filters that just need to set the
 */
public class MessageFilter extends TabFilter {

    public MessageFilter() {
        super(MessageAction.ID);
    }

    @Override
    public Pattern getPattern() {
        try {
            // Quickly update the pattern
            MessagePatterns messege = TabbyChat.getInstance().serverSettings.general.messegePattern.getValue();
            String pattern = String.format("(?:%s|%s)", messege.getOutgoing(), messege.getIncoming());
            setPattern(pattern);
        } catch (PatternSyntaxException e) {
            TabbyChat.getLogger().error(e);
        }
        return super.getPattern();
    }

    public static class MessageAction implements IFilterAction {

        public static final String ID = "Message";

        @Override
        public void action(Filter filter, FilterEvent event) {
            if (TabbyChat.getInstance().serverSettings.general.pmEnabled.getValue()) {
                // 0 = whole message, 1 = outgoing recipient, 2 = incoming recipient
                String player = event.matcher.group(1);
                // For when it's an incoming message.
                if (player == null) {
                    player = event.matcher.group(2);
                }
                Channel dest = TabbyAPI.getAPI().getChat().getChannel(player, true);
                event.channels.add(dest);
            }
        }
    }
}
