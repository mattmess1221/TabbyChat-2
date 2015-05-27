package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;

public interface ChatRecievedListener extends TabbyListener {

    void onChatRecieved(ChatRecievedEvent message);

}
