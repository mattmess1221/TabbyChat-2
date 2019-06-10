package mnm.mods.tabbychat.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.client.AbstractChannel;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.client.gui.settings.GuiSettingsScreen;
import mnm.mods.tabbychat.util.LocalVisibility;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.TexturedModal;
import mnm.mods.tabbychat.client.gui.component.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

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
                if (Screen.hasShiftDown()) {
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
        if (mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen()
                || (status != null && status.compareTo(ChannelStatus.PINGED) > 0)
                || TabbyChatClient.getInstance().getSettings().advanced.visibility.get() == LocalVisibility.ALWAYS) {
            ILocation loc = getLocation();
            GlStateManager.enableBlend();
            GlStateManager.color4f(1, 1, 1, (float) mc.gameSettings.chatOpacity);
            drawModalCorners(getStatusModal(loc.contains(mouseX, mouseY)));

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

    private TexturedModal getStatusModal(boolean hovered) {
        if (hovered) {
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
