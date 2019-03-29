package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.Color;
import mnm.mods.util.Dim;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class ChatTab extends GuiButton {

    private static final TexturedModal ACTIVE = new TexturedModal(ChatBox.GUI_LOCATION, 0, 0, 50, 14);
    private static final TexturedModal UNREAD = new TexturedModal(ChatBox.GUI_LOCATION, 50, 0, 50, 14);
    private static final TexturedModal PINGED = new TexturedModal(ChatBox.GUI_LOCATION, 100, 0, 50, 14);
    private static final TexturedModal HOVERED = new TexturedModal(ChatBox.GUI_LOCATION, 150, 0, 50, 14);
    private static final TexturedModal NONE = new TexturedModal(ChatBox.GUI_LOCATION, 200, 0, 50, 14);

    private final Channel channel;

    public ChatTab(Channel channel) {
        super(channel.getAlias());
        this.channel = channel;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (getLocation().contains(mouseX, mouseY)) {
            if (button == 0) {
                if (GuiScreen.isShiftKeyDown()) {
                    // Remove channel
                    TabbyChatClient.getInstance().getChat().removeChannel(this.channel);
                } else {
                    // Enable channel, disable others
                    TabbyChatClient.getInstance().getChat().setActiveChannel(this.channel);
                }
            } else if (button == 1) {
                // Open channel options
                this.channel.openSettings();
            } else if (button == 2) {
                // middle click
                TabbyChatClient.getInstance().getChat().removeChannel(this.channel);
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        ChannelStatus status = channel.getStatus();
        if (mc.ingameGUI.getChatGUI().getChatOpen()
                || (status != null && status.compareTo(ChannelStatus.PINGED) > 0)
                || TabbyChatClient.getInstance().getSettings().advanced.visibility.get() == ChatVisibility.ALWAYS) {
            ILocation loc = getLocation();
            GlStateManager.enableBlend();
            GlStateManager.color4f(1, 1, 1, (float) mc.gameSettings.chatOpacity);
            drawModalCorners(getStatusModal());

            int txtX = loc.getXCenter();
            int txtY = loc.getYCenter() - 2;

            Color primary = getPrimaryColorProperty();
            int color = Color.getColor(primary.getRed(), primary.getGreen(), primary.getBlue(), (int) (mc.gameSettings.chatOpacity * 255));
            this.drawCenteredString(mc.fontRenderer, this.getText(), txtX, txtY, color);
            GlStateManager.disableBlend();
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
    public Dim getMinimumSize() {
        return new Dim(mc.fontRenderer.getStringWidth(getText()) + 8, 14);
    }
}
