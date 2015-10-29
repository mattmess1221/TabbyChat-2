package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;

public interface ChannelListener extends TabbyListener {

    void onMessageAdded(MessageAddedToChannelEvent event);
}
