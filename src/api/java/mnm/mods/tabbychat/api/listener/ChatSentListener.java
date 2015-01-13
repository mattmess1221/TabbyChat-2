package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentFilterEvent;

public interface ChatSentListener extends TabbyListener {

    void onChatSent(ChatSentEvent sendEvent);

    void onChatSentFilter(ChatSentFilterEvent sendEvent);
}
