package mnm.mods.tabbychat.gui;

import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.gui.IGui;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.Color;
import mnm.mods.util.config.Value;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.FlowLayout;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.ILayout;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import net.minecraft.client.gui.Gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.Map;
import javax.annotation.Nonnull;

public class ChatTray extends GuiPanel implements IGui {

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
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen()) {
//            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getSecondaryColorProperty().getHex());
//            drawBorders(0, 0, getBounds().width, getBounds().height, getPrimaryColorProperty().getHex());
        }
        super.drawComponent(mouseX, mouseY);
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        getParent()
                .map(GuiComponent::getSecondaryColorProperty)
                .map(color -> Color.of(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 4 * 3))
                .ifPresent(this::setSecondaryColor);
    }

    public void addChannel(Channel channel) {
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

        addChannel(ChatChannel.DEFAULT_CHANNEL);
        ChatChannel.DEFAULT_CHANNEL.setStatus(ChannelStatus.ACTIVE);
    }

    @Nonnull
    @Override
    public Dimension getMinimumSize() {
        return tabList.getLayout()
                .map(ILayout::getLayoutSize)
                .orElseGet(super::getMinimumSize);
    }

    boolean isHandleHovered() {
        return handle.isHovered();
    }

    @Override
    public Rectangle getBounds() {
        return this.getLocation().asRectangle();
    }

    private class ToggleButton extends GuiComponent {

        private Value<Boolean> value;

        ToggleButton() {
            this.value = TabbyChat.getInstance().settings.advanced.keepChatOpen;
        }

        @Override
        public void drawComponent(int mouseX, int mouseY) {
            drawBorders(2, 2, 7, 7, 0xff999999);
            Gui.drawRect(2, 2, 7, 7, getSecondaryColorProperty().getHex());
            if (value.get()) {
                Gui.drawRect(3, 3, 6, 6, 0xffaaaaaa);
            }
        }

        @Subscribe
        public void action(ActionPerformedEvent event) {
            value.set(!value.get());
        }

        @Override
        @Nonnull
        public Dimension getMinimumSize() {
            return new Dimension(8, 8);
        }
    }

}
