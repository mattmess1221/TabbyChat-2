package mnm.mods.tabbychat.gui;

import java.awt.Rectangle;
import java.util.Map;
import java.util.Set;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.tabbychat.util.ChatChannel;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import net.minecraft.client.renderer.GlStateManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ChatBox extends GuiPanel implements Chat {

    private static ColorSettings colors = TabbyChat.getInstance().colorSettings;

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private Map<String, Channel> allChannels = Maps.newHashMap();
    private Set<Channel> channels = Sets.newHashSet(ChatChannel.DEFAULT_CHANNEL);
    private Channel active = ChatChannel.DEFAULT_CHANNEL;

    public ChatBox(Rectangle rect) {
        super();
        this.setLayout(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(), BorderLayout.Position.SOUTH);
        this.setBounds(rect);
        this.setForeColor(colors.chatTxtColor.getValue().getColor());
        this.setBackColor(colors.chatBoxColor.getValue().getColor());
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {

        float scale = GuiNewChatTC.getInstance().getChatScale();

        GlStateManager.pushMatrix();
        // Reset the matrix so we can have a clean slate
        GlStateManager.loadIdentity();
        // Translate it so everything's drawn correctly
        GlStateManager.translate(0f, 0f, -2000f);
        // Scale it accordingly
        GlStateManager.scale(scale, scale, 1.0F);

        // Make the upper left corner of the panel (0,0).
        GlStateManager.translate(this.getBounds().x, this.getBounds().y, 0.0F);
        super.drawComponent(mouseX, mouseY);

        GlStateManager.popMatrix();

    }

    public int getWidth() {
        return getBounds().width;
    }

    public ChatArea getChatArea() {
        return this.chatArea;
    }

    public ChatTray getTray() {
        return this.pnlTray;
    }

    public TextBox getChatInput() {
        return this.txtChatInput;
    }

    @Override
    public Channel getChannel(String name) {
        if (!allChannels.containsKey(name)) {
            allChannels.put(name, new ChatChannel(name, channels.size()));
        }
        return allChannels.get(name);
    }

    @Override
    public void addChannel(Channel channel) {
        if (!this.channels.contains(channel)) {
            this.channels.add(channel);
            pnlTray.addChannel(channel);
        }
    }

    @Override
    public void removeChannel(Channel channel) {
        if (channels.contains(channel) && !channel.equals(ChatChannel.DEFAULT_CHANNEL)) {
            channels.remove(channel);
            pnlTray.removeChannel(channel);
        }
    }

    @Override
    public Channel[] getChannels() {
        return channels.toArray(new Channel[channels.size()]);
    }

    @Override
    public void removeMessages(int id) {
        for (Channel channel : this.channels) {
            channel.removeMessages(id);
        }
    }

    @Override
    public void clearMessages() {
        // TODO save messages somewhere
        for (Channel channel : channels) {
            channel.clear();
        }

        this.channels.clear();
        this.channels.add(ChatChannel.DEFAULT_CHANNEL);

        getTray().clear();
    }

    @Override
    public Channel getActiveChannel() {
        return active;
    }

    @Override
    public void setActiveChannel(Channel channel) {
        active.setActive(false);
        active = channel;
        active.setActive(true);
    }
}
