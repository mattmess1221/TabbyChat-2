package mnm.mods.tabbychat.gui;

import com.google.common.eventbus.Subscribe;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.Dimension;
import javax.annotation.Nonnull;

public class ChatTab extends GuiButton {

    private final Channel channel;

    public ChatTab(Channel channel) {
        super(channel.getAlias());
        this.channel = channel;
    }

    @Subscribe
    public void tryCommitSudoku(GuiMouseEvent event) {
        if (event.getType() == MouseEvent.CLICK) {
            if (event.getButton() == 0) {
                if (GuiScreen.isShiftKeyDown()) {
                    // Remove channel
                    TabbyChat.getInstance().getChat().removeChannel(this.channel);
                } else {
                    // Enable channel, disable others
                    TabbyChat.getInstance().getChat().setActiveChannel(this.channel);
                }
            } else if (event.getButton() == 1) {
                // Open channel options
                this.channel.openSettings();
            } else if (event.getButton() == 2) {
                // middle click
                TabbyChat.getInstance().getChat().removeChannel(this.channel);
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        ChannelStatus status = channel.getStatus();
        if (GuiNewChatTC.getInstance().getChatOpen()
                || (status != null && status.compareTo(ChannelStatus.PINGED) > 0)
                || TabbyChat.getInstance().settings.advanced.visibility.get() == ChatVisibility.ALWAYS) {
            ILocation loc = getLocation();
            Gui.drawRect(0, 0, loc.getWidth(), loc.getHeight(), getSecondaryColorProperty().getHex());
            int txtX = loc.getWidth() / 2;
            int txtY = loc.getHeight() / 2 - this.mc.fontRenderer.FONT_HEIGHT / 2;
            this.drawCenteredString(mc.fontRenderer, this.getText(), txtX, txtY, getPrimaryColorProperty().getHex());

            this.drawVerticalLine(loc.getWidth(), -1, loc.getHeight(), super.getPrimaryColorProperty().getHex());
        }
    }

    @Override
    public String getText() {
        String alias = channel.getAlias();

        if (channel.isPm()) {
            alias = "@" + alias;
        }
        ChannelStatus status = channel.getStatus();
        if (status != null) {
            switch (status) {
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

    @Nonnull
    @Override
    public Color getSecondaryColorProperty() {
        int back = super.getSecondaryColorProperty().getHex();
        ChannelStatus status = channel.getStatus();
        if (status != null) {
            switch (status) {
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
        return applyTransparency(Color.of(back));
    }

    @Nonnull
    @Override
    public Color getPrimaryColorProperty() {
        int fore = 0xfff0f0f0;
        ChannelStatus status = channel.getStatus();
        if (status != null) {
            switch (status) {
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
        return applyTransparency(Color.of(fore));
    }

    private Color applyTransparency(Color color) {
        float perc = (mc.gameSettings.chatOpacity * 0.9F + 0.1F) / 2;
        int opacity = (int) (perc * color.getAlpha());
        return Color.of(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    @Nonnull
    @Override
    public Dimension getMinimumSize() {
        String alias = channel.getAlias();
        if (channel.isPm()) {
            alias = "@" + alias;
        }
        return new Dimension(mc.fontRenderer.getStringWidth("<" + alias + ">") + 8, 14);
    }
}
