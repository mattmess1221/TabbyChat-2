package mnm.mods.tabbychat.client.gui;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.client.AbstractChannel;
import mnm.mods.tabbychat.client.DefaultChannel;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.TexturedModal;
import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout;
import mnm.mods.tabbychat.client.gui.component.layout.FlowLayout;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;
import mnm.mods.tabbychat.client.gui.component.GuiPanel;
import mnm.mods.tabbychat.client.gui.component.layout.ILayout;

import java.util.Map;
import javax.annotation.Nonnull;

public class ChatTray extends GuiPanel {

    private final static TexturedModal MODAL = new TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 202);

    private GuiPanel tabList = new GuiPanel(new FlowLayout());
    private GuiComponent handle = new ChatHandle();

    private Map<Channel, GuiComponent> map = Maps.newHashMap();


    ChatTray() {
        super(new BorderLayout());
        this.add(tabList, BorderLayout.Position.CENTER);
        ChatPanel controls = new ChatPanel(new FlowLayout());
        controls.add(new ToggleButton());
        controls.add(handle);
        this.add(controls, BorderLayout.Position.EAST);

    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        if (mc.ingameGUI.getChatGUI().getChatOpen()) {
            GlStateManager.enableBlend();
            GlStateManager.color4f(1, 1, 1, (float) mc.gameSettings.chatOpacity);
            drawModalCorners(MODAL);
            GlStateManager.disableBlend();
        }
        super.render(mouseX, mouseY, parTicks);
    }

    @Override
    public void tick() {
        super.tick();
        getParent()
                .map(GuiComponent::getSecondaryColorProperty)
                .map(color -> Color.of(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 4 * 3))
                .ifPresent(this::setSecondaryColor);
    }

    public void addChannel(AbstractChannel channel) {
        GuiComponent gc = new ChatTab(channel);
        map.put(channel, gc);
        tabList.add(gc);
    }

    public void removeChannel(final Channel channel) {
        GuiComponent gc = map.get(channel);
        this.tabList.remove(gc);
        map.remove(channel);
    }

    public void clearMessages() {
        this.tabList.clear();

        addChannel(DefaultChannel.INSTANCE);
        ChatBox.getInstance().setStatus(DefaultChannel.INSTANCE, ChannelStatus.ACTIVE);
    }

    @Nonnull
    @Override
    public Dim getMinimumSize() {
        return tabList.getLayout()
                .map(ILayout::getLayoutSize)
                .orElseGet(super::getMinimumSize);
    }

    boolean isHandleHovered(double x, double y) {
        return handle.getLocation().contains(x, y);
    }

    private class ToggleButton extends GuiComponent {

        private Value<Boolean> value;

        ToggleButton() {
            this.value = TabbyChatClient.getInstance().getSettings().advanced.keepChatOpen;
        }

        @Override
        public void render(int mouseX, int mouseY, float parTicks) {
            GlStateManager.enableBlend();
            ILocation loc = getLocation();
            int opac = (int) (mc.gameSettings.chatOpacity * 255) << 24;
            renderBorders(loc.getXPos() + 2, loc.getYPos() + 2, loc.getXWidth() - 2, loc.getYHeight() - 2, 0x999999 | opac);
            if (value.get()) {
                fill(loc.getXPos() + 3, loc.getYPos() + 3, loc.getXWidth() - 3, loc.getYHeight() - 3, 0xaaaaaa | opac);
            }
        }

        @Override
        public boolean mouseClicked(double x, double y, int button) {
            if (getLocation().contains(x, y) && button == 0) {
                value.set(!value.get());
                return true;
            }
            return false;
        }

        @Override
        public ILocation getLocation() {
            return super.getLocation().copy().move(0, 2);
        }

        @Override
        @Nonnull
        public Dim getMinimumSize() {
            return new Dim(8, 8);
        }
    }
}
