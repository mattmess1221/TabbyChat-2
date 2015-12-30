package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.util.Iterator;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.gui.IGui;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.Color;
import mnm.mods.util.config.SettingValue;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.FlowLayout;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.events.ActionPerformed;
import mnm.mods.util.gui.events.GuiEvent;
import net.minecraft.client.gui.Gui;

public class ChatTray extends GuiPanel implements IGui<GuiPanel> {

    private GuiPanel tabList = new GuiPanel(new FlowLayout());
    private ChatPanel controls = new ChatPanel(new FlowLayout());
    private GuiComponent handle = new ChatHandle();

    public ChatTray() {
        super(new BorderLayout());
        this.addComponent(tabList, BorderLayout.Position.CENTER);
        controls.addComponent(new ToggleButton());
        controls.addComponent(handle);
        this.addComponent(controls, BorderLayout.Position.EAST);

        addChannel(ChatChannel.DEFAULT_CHANNEL);
        ChatChannel.DEFAULT_CHANNEL.setStatus(ChannelStatus.ACTIVE);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            drawBorders(0, 0, getBounds().width, getBounds().height, getForeColor());
        }
        super.drawComponent(mouseX, mouseY);
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        Color color = new Color(getParent().getBackColor());
        Color bkg = new Color(color.getRed(), color.getGreen(), color.getBlue(),
                color.getAlpha() / 4 * 3);
        this.setBackColor(bkg.getColor());
    }

    public void addChannel(Channel channel) {
        GuiComponent gc = new ChatTab(channel);
        tabList.addComponent(gc);
    }

    public void removeChannel(Channel channel) {
        boolean found = false;
        Iterator<GuiComponent> iter = this.tabList.iterator();
        while (iter.hasNext() && !found) {
            GuiComponent gc = iter.next();
            if (gc instanceof ChatTab && ((ChatTab) gc).getChannel().equals(channel)) {
                tabList.removeComponent(gc);
                found = true;
            }
        }
    }

    public void clear() {
        this.tabList.clearComponents();

        addChannel(ChatChannel.DEFAULT_CHANNEL);
        ChatChannel.DEFAULT_CHANNEL.setStatus(ChannelStatus.ACTIVE);
    }

    @Override
    public Dimension getMinimumSize() {
        return tabList.getLayout().getLayoutSize();
    }

    public boolean isHandleHovered() {
        return handle.isHovered();
    }

    @Override
    public GuiPanel asGui() {
        return this;
    }

    private class ToggleButton extends GuiComponent implements ActionPerformed {

        private SettingValue<Boolean> value;

        public ToggleButton() {
            this.value = TabbyChat.getInstance().settings.advanced.keepChatOpen;
        }

        @Override
        public void drawComponent(int mouseX, int mouseY) {
            drawBorders(2, 2, 7, 7, 0xff999999);
            Gui.drawRect(2, 2, 7, 7, getBackColor());
            if (value.getValue()) {
                Gui.drawRect(3, 3, 6, 6, 0xffaaaaaa);
            }
        }

        @Override
        public void action(GuiEvent event) {
            value.setValue(!value.getValue());
            TabbyChat.getInstance().settings.saveSettingsFile();
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(8, 8);
        }
    }
}
