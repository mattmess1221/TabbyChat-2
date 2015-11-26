package mnm.mods.tabbychat.core;

import java.util.List;

import org.lwjgl.input.Mouse;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.gui.ChatBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;

public class GuiNewChatTC extends GuiNewChat {

    private static GuiNewChatTC instance;
    private ChatManager chat;

    private TabbyChat tc = TabbyChat.getInstance();

    private GuiNewChatTC(Minecraft minecraft) {
        super(minecraft);
        chat = new ChatManager();
    }

    public static GuiNewChatTC getInstance() {
        if (instance == null) {
            instance = new GuiNewChatTC(Minecraft.getMinecraft());
        }
        return instance;
    }

    @Override
    public void refreshChat() {
        chat.getChatBox().updateComponent();
    }

    @Override
    public void drawChat(int i) {
        if (getChatOpen())
            return;
        ChatBox chatbox = chat.getChatBox();
        float scale = chatbox.getScale();

        GlStateManager.popMatrix(); // ignore what GuiIngame did.
        // translate to above the itemrenderer
        // before push so it effects the tab list too.
        GlStateManager.translate(0, 0, 150.5);
        GlStateManager.pushMatrix();

        // Scale it accordingly
        GlStateManager.scale(scale, scale, 1.0F);

        // Make the upper left corner of the panel (0,0).
        GlStateManager.translate(chatbox.getBounds().x, chatbox.getBounds().y, 0.0F);

        int mouseX = Mouse.getEventX();
        int mouseY = -Mouse.getEventY() - 1;
        chatbox.drawComponent(mouseX, mouseY);

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix(); // push to avoid gl errors
    }

    @Override
    public synchronized void printChatMessageWithOptionalDeletion(IChatComponent ichat, int id) {
        // chat listeners
        ChatRecievedEvent chatevent = new ChatRecievedEvent(ichat, id);
        chatevent.channels.add(ChatChannel.DEFAULT_CHANNEL);
        tc.getEventManager().onChatRecieved(chatevent);
        // chat filters
        ichat = chatevent.chat;
        id = chatevent.id;
        if (ichat != null && !ichat.getUnformattedText().isEmpty()) {
            if (id != 0) {
                // send removable msg to current channel
                chatevent.channels.clear();
                chatevent.channels.add(this.chat.getActiveChannel());
            }
            if (chatevent.channels.contains(ChatChannel.DEFAULT_CHANNEL) && chatevent.channels.size() > 1
                    && !tc.serverSettings.general.useDefaultTab.get()) {
                chatevent.channels.remove(ChatChannel.DEFAULT_CHANNEL);
            }
            boolean msg = !chatevent.channels.contains(this.chat.getActiveChannel());
            for (Channel channel : chatevent.channels) {
                channel.addMessage(ichat, id);
                if (msg) {
                    channel.setStatus(ChannelStatus.UNREAD);
                }
            }
            TabbyChat.getLogger().info("[CHAT] " + ichat.getUnformattedText());
            this.chat.getChatBox().updateComponent();
        }
    }

    @Override
    public void resetScroll() {
        chat.getChatBox().getChatArea().resetScroll();
        super.resetScroll();
    }

    @Override
    public List<String> getSentMessages() {
        return super.getSentMessages();
    }

    public ChatManager getChatManager() {
        return chat;
    }

    @Override
    public IChatComponent getChatComponent(int clickX, int clickY) {
        return chat.getChatBox().getChatArea().getChatComponent(clickX, clickY);
    }

    @Override
    public int getChatHeight() {
        return chat.getChatBox().getChatArea().getBounds().height;
    }

    @Override
    public int getChatWidth() {
        return chat.getChatBox().getChatArea().getBounds().width;
    }
}
