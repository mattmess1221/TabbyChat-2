package mnm.mods.tabbychat.core;

import java.awt.Rectangle;
import java.util.List;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.settings.AdvancedSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;

import org.lwjgl.input.Mouse;

public class GuiNewChatTC extends GuiNewChat {

    private static GuiNewChatTC instance;
    private ChatBox chatbox;

    private TabbyChat tc = TabbyChat.getInstance();

    private GuiNewChatTC(Minecraft minecraft) {
        super(minecraft);
        Rectangle rect = new Rectangle();

        AdvancedSettings settings = TabbyChat.getInstance().advancedSettings;
        rect.x = settings.chatX.getValue();
        rect.y = settings.chatY.getValue();
        rect.width = settings.chatW.getValue();
        rect.height = settings.chatH.getValue();

        chatbox = new ChatBox(rect);
    }

    public static GuiNewChatTC getInstance() {
        if (instance == null) {
            instance = new GuiNewChatTC(Minecraft.getMinecraft());
        }
        return instance;
    }

    @Override
    public void refreshChat() {
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
        tc.getEventManager().onChatRecieved(chatevent);
        // chat filters
        chat = chatevent.chat;
        id = chatevent.id;
        if (chat != null && !chat.getUnformattedText().isEmpty()) {
            if (id != 0) {
                // send removable msg to current channel
                chatevent.channels.clear();
                chatevent.channels.add(chatbox.getActiveChannel());
            }
            if (chatevent.channels.contains(ChatChannel.DEFAULT_CHANNEL) && chatevent.channels.size() > 1
                    && !tc.serverSettings.useDefaultTab.getValue()) {
                chatevent.channels.remove(ChatChannel.DEFAULT_CHANNEL);
            }
            boolean msg = !chatevent.channels.contains(chatbox.getActiveChannel());
            for (Channel channel : chatevent.channels) {
                channel.addMessage(chat, id);
                if (msg) {
                    channel.setStatus(ChannelStatus.UNREAD);
                }
            }
            TabbyChat.getLogger().info("[CHAT] " + chat.getUnformattedText());
            chatbox.updateComponent();
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
    public int getChatHeight() {
        return chatbox.getChatArea().getBounds().height;
    }

    @Override
    public int getChatWidth() {
        return chatbox.getChatArea().getBounds().width;
    }
}
