package mnm.mods.tabbychat.gui;

import com.google.common.collect.Maps;
import mnm.mods.tabbychat.AbstractChannel;
import mnm.mods.tabbychat.DefaultChannel;
import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.util.Color;
import mnm.mods.util.Dim;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.config.Value;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.FlowLayout;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.ILayout;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Map;
import javax.annotation.Nonnull;

public class ChatTray extends GuiPanel {

    private final static TexturedModal MODAL = new TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 202);

    private GuiPanel tabList = new GuiPanel(new FlowLayout());
    private GuiComponent handle = new ChatHandle();

    private Map<Channel, GuiComponent> map = Maps.newHashMap();


    ChatTray() {
        super(new BorderLayout());
        this.addComponent(tabList, BorderLayout.Position.CENTER);
        ChatPanel controls = new ChatPanel(new FlowLayout());
        controls.addComponent(new ToggleButton());
        controls.addComponent(handle);
        this.addComponent(controls, BorderLayout.Position.EAST);

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
        tabList.addComponent(gc);
    }

    public void removeChannel(final Channel channel) {
        GuiComponent gc = map.get(channel);
        this.tabList.removeComponent(gc);
        map.remove(channel);
    }

    public void clear() {
        this.tabList.clearComponents();

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
            drawBorders(loc.getXPos() + 2, loc.getYPos()+ 2, loc.getXWidth()-2, loc.getYHeight()-2, 0x999999 | opac);
            if (value.get()) {
                Gui.drawRect(loc.getXPos() + 3, loc.getYPos() + 3,loc.getXWidth() -3, loc.getYHeight() -3, 0xaaaaaa | opac);
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
        @Nonnull
        public Dim getMinimumSize() {
            return new Dim(8, 8);
        }
    }

}
