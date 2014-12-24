package mnm.mods.tabbychat;

import java.util.List;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.events.AddonInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;
import net.minecraft.util.IChatComponent;

public class ChatAddonAntiSpam implements ChatRecievedListener {

    private String lastMessage = "";
    private int spamCounter = 1;
    private int lineCounter = 1;

    @Override
    public void initAddon(AddonInitEvent init) {
    }

    @Override
    public void onChatRecievedFilter(ChatRecievedFilterEvent message) {
        if (TabbyChat.getInstance().generalSettings.antiSpam.getValue() && message.id == 0) {

			IChatComponent chat = message.chat;
            List<Message> messages = TabbyAPI.getAPI().getChat().getMessages();
			
            if (!this.lastMessage.isEmpty() && chat.getUnformattedText().equals(this.lastMessage)) {	
                if(lineCounter == 1){
                    do{
                        spamCounter++;
                        lineCounter++;
                        TabbyAPI.getAPI().getChat().removeMessageAt(1);
                        messages = TabbyAPI.getAPI().getChat().getMessages();
                    }while(messages.get(1).getMessage().getUnformattedText().equals(this.lastMessage));				
                    chat.appendText(" [" + spamCounter + "x]");		
                }else{
                    lineCounter--;
                }
            } else {
                spamCounter = 1;
                lineCounter = 1;
                this.lastMessage = chat.getUnformattedText();
            }
        }
    }

    @Override
    public void onChatRecieved(ChatRecievedEvent message) {
    }
}
