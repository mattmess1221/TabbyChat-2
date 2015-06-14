package mnm.mods.tabbychat.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Map;
import java.util.Set;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.settings.AdvancedSettings;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Mouse;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ChatBox extends GuiPanel implements Chat, GuiMouseAdapter {

    private static ColorSettings colors = TabbyChat.getInstance().colorSettings;

    private ChatArea chatArea;
    private ChatTray pnlTray;
    private TextBox txtChatInput;

    private Map<String, Channel> allChannels = Maps.newHashMap();
    private Set<Channel> channels = Sets.newHashSet(ChatChannel.DEFAULT_CHANNEL);
    private Channel active = ChatChannel.DEFAULT_CHANNEL;

    private Point drag;
    private Rectangle tempbox;

    public ChatBox(Rectangle rect) {
        super();
        this.setLayout(new BorderLayout());
        this.addComponent(pnlTray = new ChatTray(), BorderLayout.Position.NORTH);
        this.addComponent(chatArea = new ChatArea(), BorderLayout.Position.CENTER);
        this.addComponent(txtChatInput = new TextBox(), BorderLayout.Position.SOUTH);
        this.addComponent(new Scrollbar(chatArea), BorderLayout.Position.EAST);
        this.setBounds(rect);
    }

    @Override
    public void accept(GuiMouseEvent event) {
        Rectangle bounds = getBounds();
        float scale = getActualScale();

        // divide by scale because smaller scales make the point movement larger
        int x = (int) (event.position.x / scale);
        int y = (int) (event.position.y / scale);

        if (event.event == GuiMouseEvent.PRESSED) {
            if (Mouse.isButtonDown(0) && (pnlTray.isHovered() || (GuiScreen.isAltKeyDown() && isHovered()))) {
                drag = new Point(x, y);
                tempbox = new Rectangle(bounds);
            }
        }

        if (drag != null) {
            if (event.event == GuiMouseEvent.RELEASED) {
                // save bounds
                AdvancedSettings sett = TabbyChat.getInstance().advancedSettings;
                sett.chatX.setValue(bounds.x);
                sett.chatY.setValue(bounds.y);

                sett.saveSettingsFile();
                drag = null;
                tempbox = null;
            } else if (event.event == GuiMouseEvent.DRAGGED) {
                bounds.setLocation(tempbox.x + x - drag.x, tempbox.y + y - drag.y);
            }
        }
    }

    @Override
    public int getForeColor() {
        return colors.chatTextColor.getValue().getColor();
    }

    @Override
    public int getBackColor() {
        return colors.chatBoxColor.getValue().getColor();
    }

    @Override
    public float getScale() {
        return GuiNewChatTC.getInstance().getChatScale();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {

        float scale = getScale();

        GlStateManager.pushMatrix();
        // Reset the matrix so it can have a clean slate
        GlStateManager.loadIdentity();
        // Translate it so everything's drawn correctly
        GlStateManager.translate(0f, 0f, -2000f);
        // Go up some more so it's in front of the hotbar.
        GlStateManager.translate(0, 0, 0x97);
        // Scale it accordingly
        GlStateManager.scale(scale, scale, 1.0F);

        // Make the upper left corner of the panel (0,0).
        GlStateManager.translate(this.getBounds().x, this.getBounds().y, 0.0F);
        super.drawComponent(mouseX, mouseY);

        GlStateManager.popMatrix();

    }

    @Override
    public void updateComponent() {
        Rectangle bounds = getBounds();
        Point point = getActualPosition();
        float scale = getActualScale();
        ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        int x = point.x;
        int y = point.y;
        int w = (int) (bounds.width * scale);
        int h = (int) (bounds.height * scale);

        int x1 = x;
        int y1 = y;

        x1 = Math.max(0, x1);
        x1 = Math.min(x1, sr.getScaledWidth() - w);
        y1 = Math.max(0, y1);
        y1 = Math.min(y1, sr.getScaledHeight() - h);

        if (x1 != x || y1 != y) {
            bounds.x = MathHelper.ceiling_double_int(x1 / scale);
            bounds.y = MathHelper.ceiling_double_int(y1 / scale);
        }
        super.updateComponent();
    }

    @Override
    public void onClosed() {
        super.onClosed();
        updateComponent();
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
            Channel chan = TabbyChat.getInstance().channelSettings.channels.getValue().get(name);
            if (chan == null) {
                chan = new ChatChannel(name);
                TabbyChat.getInstance().channelSettings.addChannel(chan);
            }
            allChannels.put(name, chan);
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
        if (getActiveChannel() == channel) {
            setActiveChannel(ChatChannel.DEFAULT_CHANNEL);
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
        GuiText text = getChatInput().getTextField();
        if (active.isPrefixHidden()
                ? text.getValue().trim().isEmpty()
                : text.getValue().trim().equals(active.getPrefix())) {
            // text is the prefix, so remove it.
            text.setValue("");
            if (!channel.isPrefixHidden() && !channel.getPrefix().isEmpty()) {
                // target has prefix visible
                text.setValue(channel.getPrefix() + " ");
            }
        }
        // reset scroll
        // TODO per-channel scroll settings?
        if (channel != active) {
            getChatArea().resetScroll();
        }
        active.setStatus(null);
        active = channel;
        active.setStatus(ChannelStatus.ACTIVE);
    }
}
