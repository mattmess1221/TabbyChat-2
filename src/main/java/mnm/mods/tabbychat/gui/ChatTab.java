package mnm.mods.tabbychat.gui;

import com.google.common.eventbus.Subscribe;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Dimension;
import javax.annotation.Nonnull;

public class ChatTab extends GuiButton {

    private static final TexturedModal ACTIVE = new TexturedModal(ChatBox.GUI_LOCATION, 0, 0, 50, 12);
    private static final TexturedModal UNREAD = new TexturedModal(ChatBox.GUI_LOCATION, 50, 0, 50, 12);
    private static final TexturedModal PINGED = new TexturedModal(ChatBox.GUI_LOCATION, 100, 0, 50, 12);
    private static final TexturedModal HOVERED = new TexturedModal(ChatBox.GUI_LOCATION, 150, 0, 50, 12);
    private static final TexturedModal NONE = new TexturedModal(ChatBox.GUI_LOCATION, 200, 0, 50, 12);

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
            GlStateManager.enableBlend();
            drawModalCorners(getStatusModal(), loc);
            GlStateManager.disableBlend();

            int txtX = loc.getWidth() / 2;
            int txtY = loc.getHeight() / 2 - 2;
            this.drawCenteredString(mc.fontRendererObj, this.getText(), txtX, txtY, getPrimaryColorProperty().getHex());
        }
    }

    private void drawModalCorners(TexturedModal modal, ILocation location) {
        int x = 0;
        int y = 1;

        int w = location.getWidth() / 2;
        int w2 = location.getWidth() - w;
        int h = location.getHeight() / 2;
        int h2 = location.getHeight() - h;

        int mx = modal.getXPos();
        int my = modal.getYPos();
        int mw = modal.getWidth() - w2;
        int mh = modal.getHeight() - h2;

        // bind the texture
        mc.getTextureManager().bindTexture(modal.getResourceLocation());

        // top left
        drawTexturedModalRect(x, y, mx, my, w, h);
        // top right
        drawTexturedModalRect(x + w, y, mx + mw, my, w2, h);
        // bottom left
        drawTexturedModalRect(x, y + h, mx, my + mh, w, h2);
        // bottom right
        drawTexturedModalRect(x + w, y + h, mx + mw, my + mh, w2, h2);
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

    private TexturedModal getStatusModal() {
        if (isHovered()) {
            return HOVERED;
        }
        ChannelStatus status = channel.getStatus();
        if (status != null) {
            switch (status) {
                case ACTIVE:
                    return ACTIVE;
                case UNREAD:
                    return UNREAD;
                case PINGED:
                    return PINGED;
            }
        }
        return NONE;
    }

    @Nonnull
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(mc.fontRendererObj.getStringWidth(getText()) + 8, 14);
    }
}
