package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class ChatTab extends GuiButton implements GuiMouseAdapter {

    private final Channel channel;

    public ChatTab(Channel channel) {
        super(channel.getAlias());
        this.channel = channel;
    }

    @Override
    public void accept(GuiMouseEvent event) {
        if (event.event == GuiMouseEvent.CLICKED) {
            ChatTab comp = (ChatTab) event.component;
            if (event.button == 0) {
                channel.setPending(false);
                if (GuiScreen.isShiftKeyDown()) {
                    // Remove channel
                    TabbyChat.getInstance().getChat().removeChannel(comp.getChannel());
                } else {
                    // Enable channel, disable others
                    TabbyChat.getInstance().getChat().setActiveChannel(comp.getChannel());
                }
            } else if (event.button == 1) {
                // Open channel options
                comp.getChannel().openSettings();
            } else if (event.button == 2) {
                // middle click
                TabbyChat.getInstance().getChat().removeChannel(comp.getChannel());
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen() || channel.isPending()) {
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            this.drawCenteredString(mc.fontRendererObj, this.getText(), this.getBounds().width / 2,
                    (this.getBounds().height - 8) / 2, getForeColor());
        }
    }

    @Override
    public void updateComponent() {
        int fore = 0xf0f0f0;
        int back = 0;
        if (channel.isActive()) {
            setText("[" + channel.getAlias() + "]");
            // Cyan
            // fore = 0x5b7c7b;
            back = 0xa5e7e4;
        } else if (channel.isPending()) {
            setText("<" + channel.getAlias() + ">");
            // Red
            fore = 0x720000;
            back = 0xff0000;
        } else {
            setText(channel.getAlias());
        }
        if (isHovered()) {
            // Yellow
            fore = 0xffffa0;
            back = 0x7f8052;
        }
        setForeColor(fore);
        setBackColor(back);
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public void setBackColor(int backColor) {
        super.setBackColor(applyTransparency(backColor));
    }

    @Override
    public void setForeColor(int foreColor) {
        super.setForeColor(applyTransparency(foreColor));
    }

    private int applyTransparency(int color) {
        float perc = (mc.gameSettings.chatOpacity * 0.9F + 0.1F) / 3;
        int opacity = (int) (255 * perc) << 24;
        return color + opacity;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(mc.fontRendererObj.getStringWidth("<" + channel.getAlias() + ">") + 8,
                15);
    }
}
