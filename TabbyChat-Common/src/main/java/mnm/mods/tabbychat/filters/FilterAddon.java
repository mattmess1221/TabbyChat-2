package mnm.mods.tabbychat.filters;

import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.filters.ChannelFilter.ChannelAction;
import mnm.mods.tabbychat.filters.ChatFilter.DefaultAction;
import mnm.mods.tabbychat.filters.MessageFilter.MessageAction;
import mnm.mods.tabbychat.settings.ServerSettings;

public class FilterAddon implements ChatRecievedListener {

    private ChatFilter channelFilter = new ChannelFilter();
    private ChatFilter messageFilter = new MessageFilter();

    public FilterAddon() {
        AddonManager addons = TabbyAPI.getAPI().getAddonManager();
        addons.registerFilterAction(MessageAction.ID, new MessageAction());
        addons.registerFilterAction(ChannelAction.ID, new ChannelAction());
        addons.registerFilterAction(DefaultAction.ID, new DefaultAction());
    }

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
