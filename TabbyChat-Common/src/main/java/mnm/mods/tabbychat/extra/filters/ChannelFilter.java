package mnm.mods.tabbychat.extra.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.settings.GeneralServerSettings;
import mnm.mods.tabbychat.util.ChannelPatterns;

public class ChannelFilter extends TabFilter {

    public ChannelFilter() {
        super(ChannelAction.ID);
    }

    @Override
    public Pattern getPattern() {
        try {
            // Quickly update the pattern
            ChannelPatterns delims = TabbyChat.getInstance().serverSettings.general.channelPattern.getValue();
            setPattern(delims.getPattern().pattern());
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
        return super.getPattern();
    }

    public static class ChannelAction implements IFilterAction {

        public static final String ID = "Channel";

        @Override
        public void action(Filter filter, FilterEvent event) {
            GeneralServerSettings general = TabbyChat.getInstance().serverSettings.general;
            if (general.channelsEnabled.getValue()) {
                String chan = event.matcher.group(1);
                if (!general.ignoredChannels.getValue().contains(chan)) {
                    // not ignoring
                    Channel dest = TabbyAPI.getAPI().getChat().getChannel(chan);
                    event.channels.add(dest);
                }
            }
        }
    }
}
