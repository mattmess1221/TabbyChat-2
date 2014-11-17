package mnm.mods.tabbychat;

import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.events.AddonInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;
import net.minecraft.util.IChatComponent;

public class ChatAddonAntiSpam implements ChatRecievedListener {

    private String lastMessage = "";
    private int spamCounter = 1;

    @Override
    public void initAddon(AddonInitEvent init) {
    }

    @Override
    public void onChatRecievedFilter(ChatRecievedFilterEvent message) {
        if (TabbyChat.getInstance().generalSettings.antiSpam.getValue() && message.id == 0) {
            IChatComponent chat = message.chat;
            if (!this.lastMessage.isEmpty() && chat.getUnformattedText().equals(this.lastMessage)) {
                spamCounter++;
                chat.appendText(" [" + spamCounter + "x]");
                TabbyAPI.getAPI().getChat().removeMessageAt(1);
            } else {
                spamCounter = 1;
                this.lastMessage = chat.getUnformattedText();
            }
        }
    }

    @Override
    public void onChatRecieved(ChatRecievedEvent message) {
    }
}
