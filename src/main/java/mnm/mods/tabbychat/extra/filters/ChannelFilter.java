package mnm.mods.tabbychat.extra.filters;

import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.settings.GeneralServerSettings;

import java.util.regex.Pattern;
import javax.annotation.Nonnull;

public class ChannelFilter implements Filter {

    private final ChatManager chat = TabbyChatClient.getInstance().getChat();

    @Nonnull
    @Override
    public Pattern getPattern() {
        return TabbyChatClient.getInstance().getServerSettings().general.channelPattern.get().getPattern();
    }

    @Override
    public void action(FilterEvent event) {

        GeneralServerSettings general = TabbyChatClient.getInstance().getServerSettings().general;
        if (general.channelsEnabled.get()) {
            String chan = event.matcher.group(1);
            Channel dest = chat.getChannel(chan);
            event.channels.add(dest);

        }

    }
}
