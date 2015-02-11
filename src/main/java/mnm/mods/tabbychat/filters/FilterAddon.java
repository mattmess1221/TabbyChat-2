package mnm.mods.tabbychat.filters;

import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.settings.ServerSettings;

public class FilterAddon implements ChatRecievedListener {

    private ChatFilter channelFilter = new ChannelFilter();
    private ChatFilter messageFilter = new MessageFilter();

    @Override
    public void onChatRecieved(ChatRecievedEvent message) {
        ServerSettings settings = TabbyChat.getInstance().serverSettings;
        if (settings == null) {
            // We're possibly not in game.
            return;
        }
        List<ChatFilter> filters = settings.filters.getValue();

        channelFilter.applyFilter(message);
        messageFilter.applyFilter(message);
        for (ChatFilter filter : filters) {
            filter.applyFilter(message);
        }
    }
}
