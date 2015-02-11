package mnm.mods.tabbychat.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.util.ChannelPatterns;

public class ChannelFilter extends TabFilter {

    private static final String PATTERN_FORMAT = "^\\%s([\\p{L}0-9_]{1,16})\\%s";

    @Override
    public Pattern getPattern() {
        try {
            // Quickly update the pattern
            ChannelPatterns delims =
                    TabbyChat.getInstance().serverSettings.channelPattern.getValue();
            String pattern = String.format(PATTERN_FORMAT, delims.getOpen(), delims.getClose());
            setPattern(pattern);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return super.getPattern();
    }

    @Override
    public void action(Filter filter, FilterEvent event) {
        String chan = event.matcher.group(1);
        Channel dest = TabbyAPI.getAPI().getChat().getChannel(chan);
        event.channels.add(dest);
    }
}
