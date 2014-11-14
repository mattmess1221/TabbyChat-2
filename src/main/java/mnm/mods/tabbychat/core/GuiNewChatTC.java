package mnm.mods.tabbychat.core;

import java.util.List;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;
import mnm.mods.tabbychat.core.api.TabbyProxy;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.util.ChatChannel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Mouse;

public class GuiNewChatTC extends GuiNewChat {

    private static GuiNewChatTC instance;
    private ChatBox chatbox;

    private GuiNewChatTC(Minecraft minecraft) {
        super(minecraft);
        chatbox = new ChatBox();
    }

    public static GuiNewChatTC getInstance() {
        if (instance == null)
            instance = new GuiNewChatTC(Minecraft.getMinecraft());
        return instance;
    }

    @Override
    public void refreshChat() {
        // TODO Auto-generated method stub
        chatbox.updateComponent();
    }

    @Override
    public void drawChat(int i) {
        int mouseX = Mouse.getEventX();
        int mouseY = -Mouse.getEventY() - 1;
        chatbox.drawComponent(mouseX, mouseY);
    }

    @Override
    public void printChatMessageWithOptionalDeletion(IChatComponent chat, int id) {
        // chat listeners
        ChatRecievedEvent chatevent = new ChatRecievedEvent(chat, id);
        chatevent.channels.add(ChatChannel.DEFAULT_CHANNEL);
        chatevent.channels.add(this.chatbox.getChannel("Test"));
        TabbyProxy.onChatRecieved(chatevent);
        // chat filters
        ChatRecievedFilterEvent chatfilter = new ChatRecievedFilterEvent(chat, id);
        TabbyProxy.onChatReicevedFilter(chatfilter);
        chat = chatfilter.chat;
        id = chatfilter.id;
        if (chat != null && !chat.getUnformattedText().isEmpty()) {
            Channel[] channels = chatevent.channels.toArray(new Channel[0]);
            chatbox.addMessage(channels, chatfilter.chat, chatfilter.id);
        }
    }

    @Override
    public void resetScroll() {
        chatbox.getChatArea().resetScroll();
        super.resetScroll();
    }

    @Override
    public void clearChatMessages() {
        chatbox.clearMessages();
        super.clearChatMessages();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getSentMessages() {
        return super.getSentMessages();
    }

    public ChatBox getChatbox() {
        return chatbox;
    }

    @Override
    public IChatComponent getChatComponent(int clickX, int clickY) {
        return chatbox.getChatArea().getChatComponent(clickX, clickY);
    }

    @Override
    public int getChatWidth() {
        return chatbox.getWidth();
    }
}
