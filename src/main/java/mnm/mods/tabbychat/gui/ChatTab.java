package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.AbstractChannel;
import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.UserChannel;
import mnm.mods.tabbychat.gui.settings.GuiSettingsScreen;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.Color;
import mnm.mods.util.Dim;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;

public class ChatTab extends GuiButton {

    private static final TexturedModal ACTIVE = new TexturedModal(ChatBox.GUI_LOCATION, 0, 0, 50, 14);
    private static final TexturedModal UNREAD = new TexturedModal(ChatBox.GUI_LOCATION, 50, 0, 50, 14);
    private static final TexturedModal PINGED = new TexturedModal(ChatBox.GUI_LOCATION, 100, 0, 50, 14);
    private static final TexturedModal HOVERED = new TexturedModal(ChatBox.GUI_LOCATION, 150, 0, 50, 14);
    private static final TexturedModal NONE = new TexturedModal(ChatBox.GUI_LOCATION, 200, 0, 50, 14);

    private final ChatBox chat = ChatBox.getInstance();
    private final AbstractChannel channel;

    ChatTab(AbstractChannel channel) {
        super(channel.getDisplayName());
        this.channel = channel;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (getLocation().contains(mouseX, mouseY)) {
            if (button == 0) {
                if (GuiScreen.isShiftKeyDown()) {
                    // Remove channel
                    chat.removeChannel(this.channel);
                } else {
                    // Enable channel, disable others
                    chat.setActiveChannel(this.channel);
                }
            } else if (button == 1) {
                // Open channel options
                openSettings();
            } else if (button == 2) {
                // middle click
                chat.removeChannel(this.channel);
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    private void openSettings() {
        Minecraft.getInstance().displayGuiScreen(new GuiSettingsScreen(channel));
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        ChannelStatus status = chat.getStatus(channel);
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
        String alias = channel.getDisplayName();

        ChannelStatus status = chat.getStatus(channel);
        if (status != null) {
            switch (status) {
                case ACTIVE: return "[" + alias + "]";
                case UNREAD: return "<" + alias + ">";
                default: return alias;
            }
        }
        return alias;
    }

    private TexturedModal getStatusModal() {
        if (isHovered()) {
            return HOVERED;
        }
        ChannelStatus status = chat.getStatus(channel);
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
        String chan = "<" + channel.getDisplayName() + ">";
        return new Dim(mc.fontRenderer.getStringWidth(chan) + 8, 14);
    }
}
