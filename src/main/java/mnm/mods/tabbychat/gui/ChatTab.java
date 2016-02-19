package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class ChatTab extends GuiButton {

    private final Channel channel;

    public ChatTab(Channel channel) {
        super(channel.getAlias());
        this.channel = channel;
    }

    @Subscribe
    public void tryCommitSudoku(GuiMouseEvent event) {
        if (event.getEvent() == MouseEvent.CLICK) {
            if (event.getButton() == 0) {
                if (GuiScreen.isShiftKeyDown()) {
                    // Remove channel
                    TabbyChat.getInstance().getChat().removeChannel(this.getChannel());
                } else {
                    // Enable channel, disable others
                    TabbyChat.getInstance().getChat().setActiveChannel(this.getChannel());
                }
            } else if (event.getButton() == 1) {
                // Open channel options
                this.getChannel().openSettings();
            } else if (event.getButton() == 2) {
                // middle click
                TabbyChat.getInstance().getChat().removeChannel(this.getChannel());
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen()
                || (channel.getStatus() != null && channel.getStatus().compareTo(ChannelStatus.PINGED) > 0)
                || TabbyChat.getInstance().settings.advanced.visibility.getValue() == ChatVisibility.ALWAYS) {
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            int txtX = this.getBounds().width / 2;
            int txtY = this.getBounds().height / 2 - this.mc.fontRendererObj.FONT_HEIGHT / 2;
            this.drawCenteredString(mc.fontRendererObj, this.getText(), txtX, txtY, getForeColor());
            // this.drawVerticalLine(0, -1, getBounds().height,
            // super.getForeColor());
            this.drawVerticalLine(getBounds().width, -1, getBounds().height, super.getForeColor());
        }
    }

    public Channel getChannel() {
        return this.channel;
    }

    @Override
    public String getText() {
        String alias = channel.getAlias();

        if (channel.isPm()) {
            alias = "@" + alias;
        }
        if (channel.getStatus() != null) {
            switch (channel.getStatus()) {
            case ACTIVE:
                alias = "[" + alias + "]";
                break;
            case UNREAD:
                alias = "<" + alias + ">";
                break;
            default:
                break;
            }
        }
        return alias;
    }

    @Override
    public int getBackColor() {
        int back = super.getBackColor();
        if (channel.getStatus() != null) {
            switch (channel.getStatus()) {
            case ACTIVE:
                // Cyan
                back = 0xff5b7c7b;
                break;
            case UNREAD:
                // Red
                back = 0xff720000;
                break;
            case PINGED:
                // green
                back = 0xff00aa00;
                break;
            case JOINED:
                // aqua
                back = 0xff00aaaa;
                break;
            }
        }
        if (isHovered()) {
            // Yellow
            back = 0xff7f8052;
        }
        return applyTransparency(back);
    }

    @Override
    public int getForeColor() {
        int fore = 0xfff0f0f0;
        if (channel.getStatus() != null) {
            switch (channel.getStatus()) {
            case ACTIVE:
                // Cyan
                fore = 0xffa5e7e4;
                break;
            case UNREAD:
                // Red
                fore = 0xffff0000;
                break;
            case PINGED:
                // green
                fore = 0xff55ff55;
                break;
            case JOINED:
                // aqua
                fore = 0xff55ffff;
                break;
            }
        }

        if (isHovered()) {
            // Yellow
            fore = 0xffffffa0;
        }
        return applyTransparency(fore);
    }

    private int applyTransparency(int color) {
        Color color1 = Color.of(color);
        float perc = (mc.gameSettings.chatOpacity * 0.9F + 0.1F) / 2;
        int opacity = (int) (perc * color1.getAlpha());
        return Color.of(color1.getRed(), color1.getGreen(), color1.getBlue(), opacity).getColor();
    }

    @Override
    public Dimension getMinimumSize() {
        String alias = channel.getAlias();
        if (channel.isPm()) {
            alias = "@" + alias;
        }
        return new Dimension(mc.fontRendererObj.getStringWidth("<" + alias + ">") + 8, 14);
    }
}
