package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentEvent;

public interface ChatSentListener extends TabbyListener {

    void onChatSent(ChatSentEvent sendEvent);
}
