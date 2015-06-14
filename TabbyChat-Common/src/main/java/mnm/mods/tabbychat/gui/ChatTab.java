package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

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
        if (GuiNewChatTC.getInstance().getChatOpen() || channel.getStatus() == ChannelStatus.PINGED) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 1);
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            this.drawCenteredString(mc.fontRendererObj, this.getText(), this.getBounds().width / 2,
                    (this.getBounds().height - 8) / 2, getForeColor());
            this.drawBorders(0, 0, getBounds().width - 1, getBounds().height, getForeColor());
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void updateComponent() {
        int fore = 0xfff0f0f0;
        int back = 0x010101;
        if (channel.getStatus() != null) {
            switch (channel.getStatus()) {
            case ACTIVE:
                setText("[" + channel.getAlias() + "]");
                // Cyan
                back = 0xff5b7c7b;
                fore = 0xffa5e7e4;
                break;
            case UNREAD:
                setText("<" + channel.getAlias() + ">");
                // Red
                back = 0xff720000;
                fore = 0xffff0000;
                break;
            case PINGED:
                // green
                fore = 0xff55ff55;
                back = 0xff00aa00;
                break;
            case JOINED:
                // aqua
                fore = 0xff55ffff;
                back = 0xff00aaaa;
                break;
            }
        } else {
            setText(channel.getAlias());
        }
        if (isHovered()) {
            // Yellow
            fore = 0xffffffa0;
            back = 0xff7f8052;
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
        Color color1 = new Color(color);
        float perc = (mc.gameSettings.chatOpacity * 0.9F + 0.1F) / 2;
        int opacity = (int) (perc * color1.getAlpha());
        return new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), opacity).getColor();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(mc.fontRendererObj.getStringWidth("<" + channel.getAlias() + ">") + 8,
                15);
    }
}
