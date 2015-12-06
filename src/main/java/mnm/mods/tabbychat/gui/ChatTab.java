package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.core.GuiNewChatTC;
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
        if (GuiNewChatTC.getInstance().getChatOpen() || channel.getStatus() == ChannelStatus.PINGED
                || (channel.getStatus() == ChannelStatus.UNREAD && channel.isPm())) {
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            this.drawCenteredString(mc.fontRendererObj, this.getText(), this.getBounds().width / 2,
                    (this.getBounds().height - 8) / 2, getForeColor());
            this.drawBorders(0, 0, getBounds().width - 1, getBounds().height, getForeColor());
        }
    }

    @Override
    public void updateComponent() {
        int fore = 0xfff0f0f0;
        int back = 0x010101;
        String alias = channel.getAlias();

        if (channel.isPm()) {
            alias = "@" + alias;
        }
        if (channel.getStatus() != null) {
            switch (channel.getStatus()) {
            case ACTIVE:
                alias = "[" + alias + "]";
                // Cyan
                back = 0xff5b7c7b;
                fore = 0xffa5e7e4;
                break;
            case UNREAD:
                alias = "<" + alias + ">";
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
        }
        setText(alias);

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
        return new Dimension(mc.fontRendererObj.getStringWidth("<" + alias + ">") + 8, 15);
    }
}
