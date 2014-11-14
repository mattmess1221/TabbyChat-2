package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;

public interface ChatRecievedListener extends TabbyAddon {

    void onChatRecieved(ChatRecievedEvent message);

    void onChatRecievedFilter(ChatRecievedFilterEvent message);

}
