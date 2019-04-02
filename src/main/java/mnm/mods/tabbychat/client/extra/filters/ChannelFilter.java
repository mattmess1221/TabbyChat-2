package mnm.mods.tabbychat.client.extra.filters;

import mnm.mods.tabbychat.client.ChatManager;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.client.settings.GeneralServerSettings;

import java.util.regex.Pattern;
import javax.annotation.Nonnull;

public class ChannelFilter implements Filter {

    private final ChatManager chat;

    public ChannelFilter(ChatManager chat) {
        this.chat = chat;
    }

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
