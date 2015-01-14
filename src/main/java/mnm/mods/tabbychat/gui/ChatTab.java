package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.core.GuiNewChatTC;

public class ChatTab extends PrefsButton {

    private final Channel channel;

    public ChatTab(Channel channel) {
        super(channel.getAlias());
        this.channel = channel;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen() || channel.isPending()) {
            super.drawComponent(mouseX, mouseY);
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
        if (hovered) {
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
