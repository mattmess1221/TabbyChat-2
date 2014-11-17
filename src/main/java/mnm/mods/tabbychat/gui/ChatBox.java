package mnm.mods.tabbychat.gui;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.settings.ChatBoxSettings;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.tabbychat.util.ChatChannel;
import mnm.mods.tabbychat.util.ChatMessage;
import mnm.mods.tabbychat.util.ChatTextUtils;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ChatBox extends GuiPanel implements Chat {

    private static ChatBoxSettings settings = TabbyChat.getInstance().chatSettings;
    private static ColorSettings colors = TabbyChat.getInstance().colorSettings;

    private final int absMinX = 0;
    private final int absMinY = 0;
    private final int absMinW = 200;
    private final int absMinH = 24;

    private Minecraft mc = Minecraft.getMinecraft();

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private Map<String, Channel> allChannels = Maps.newHashMap();
    private List<Channel> channels = Lists.newArrayList(ChatChannel.DEFAULT_CHANNEL);
    private List<Message> recievedChat = Lists.newArrayList();

    private ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

    public ChatBox() {
        this(getSavedRect());
    }

    private static Rectangle getSavedRect() {
        int x = settings.xPos.getValue();
        int y = settings.yPos.getValue();
        int w = settings.width.getValue();
        int h = settings.height.getValue();
        return new Rectangle(x, y, w, h);
    }

    public ChatBox(Rectangle rect) {
        super();
        this.setLayout(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(this), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(this), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(this), BorderLayout.Position.SOUTH);
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

        super.drawComponent(mouseX, mouseY);

        GlStateManager.popMatrix();

    }

    private void updateResolution() {
        sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    }

    private void enforceScreenBoundary(Rectangle bounds) {
        updateResolution();

        // Ensure the chat is not moved off screen.
        bounds.x = Math.max(bounds.x, absMinX);
        bounds.y = Math.max(bounds.y, absMinY);
        if (bounds.getMaxX() + 4 > sr.getScaledWidth())
            bounds.x = sr.getScaledWidth() - bounds.width - 4;
        if (bounds.getMaxY() + pnlTray.getBounds().height + 4 > absMinY + sr.getScaledHeight())
            bounds.y = absMinY + sr.getScaledHeight() - bounds.height - pnlTray.getBounds().height
            - 4;

        // Ensure the chat doesn't get too small.
        bounds.width = Math.max(bounds.width, absMinW);
        bounds.height = Math.max(bounds.height, absMinH);

        // Set the new position.
        getBounds().setBounds(bounds);
        pnlTray.setBounds(bounds.x, bounds.y - pnlTray.getBounds().height, bounds.width,
                pnlTray.getBounds().height);

        // Save the new position.
        ChatBoxSettings settings = TabbyChat.getInstance().chatSettings;
        settings.xPos.setValue(bounds.x);
        settings.yPos.setValue(bounds.y);
        settings.width.setValue(bounds.width);
        settings.height.setValue(bounds.height);
        settings.saveSettingsFile();
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
    public void addMessage(Channel channel, IChatComponent chat) {
        addMessage(channel, chat, 0);
    }

    @Override
    public void addMessage(Channel channel, IChatComponent chat, int id) {
        addMessage(new Channel[] { channel }, chat, id);
    }

    @Override
    public void addMessage(Channel[] channels, IChatComponent chat) {
        addMessage(channels, chat, 0);

    }

    @Override
    public void addMessage(Channel[] channels, IChatComponent chat, int id) {
        int counter = Minecraft.getMinecraft().ingameGUI.getUpdateCounter();
        addMessage(channels, chat, id, counter, true);
    }

    public void addMessage(Channel[] channels, IChatComponent chat, int id, int counter, boolean add) {
        if (id != 0) {
            removeMessages(id);
        }
        boolean hasActive = false;
        for (Channel chan : channels) {
            if (!this.channels.contains(chan)) {
                this.channels.add(chan);
                getTray().addChannel(chan);
            }
            if (this.channels.contains(chan)) {
                hasActive = true;
            }
        }
        if (!hasActive) {
            for (Channel chan : channels) {
                chan.setPending(true);
            }
        }
        if (add) {
            // Add message to recieved chat
            Message line = new ChatMessage(counter, chat, id);
            line.addChannel(channels);
            recievedChat.add(0, line);
        }
        List<IChatComponent> list = ChatTextUtils.split(chat, getChatArea().getBounds().width);
        for (IChatComponent part : list) {
            // Add split message lines to chat area.
            Message line = new ChatMessage(counter, part, id);
            line.addChannel(channels);
            getChatArea().addChatLine(line);
        }
    }

    public void refreshChat() {
        chatArea.clearMessages();
        for (Message channel : recievedChat) {
            this.addMessage(channel.getChannels(), channel.getMessage(), channel.getID(),
                    channel.getCounter(), false);
        }
    }

    @Override
    public void removeMessages(int id) {
        Iterator<Message> iter = this.recievedChat.iterator();

        while (iter.hasNext()) {
            Message line = iter.next();
            if (line.getID() == id) {
                iter.remove();
            }
        }

        getChatArea().removeChatLines(id);
    }

    @Override
    public void removeMessageAt(int pos) {
        recievedChat.remove(pos);

    }

    @Override
    public List<Message> getMessages() {
        return recievedChat;
    }

    @Override
    public void clearMessages() {
        // TODO save messages somewhere
        recievedChat.clear();
        refreshChat();

        this.channels.clear();
        this.channels.add(ChatChannel.DEFAULT_CHANNEL);

        getTray().clear();
    }

}
