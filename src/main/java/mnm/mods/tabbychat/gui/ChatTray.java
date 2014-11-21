package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.util.Iterator;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatChannel;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.FlowLayout;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiPanel;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public class ChatTray extends GuiPanel {

    private GuiPanel tabList = new GuiPanel(new FlowLayout());
    private GuiPanel controls = new GuiPanel(new FlowLayout());

    private int count = 0;

    public ChatTray() {
        super(new BorderLayout());
        this.addComponent(tabList, BorderLayout.Position.WEST);
        this.addComponent(controls, BorderLayout.Position.EAST);
        addChannel(ChatChannel.DEFAULT_CHANNEL);
        ChatChannel.DEFAULT_CHANNEL.setActive(true);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        super.drawComponent(mouseX, mouseY);
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            drawBorders(0, 0, getBounds().width, getBounds().height);
        }
    }

    public void addChannel(Channel channel) {
        channel.setPosition(count);
        GuiComponent gc = new ChatTab(channel);
        gc.addEventListener(new ChannelClickListener());
        tabList.addComponent(gc);
        count++;
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
        count--;
    }

    public void clear() {
        this.tabList.clearComponents();
        this.count=0;

        addChannel(ChatChannel.DEFAULT_CHANNEL);
        ChatChannel.DEFAULT_CHANNEL.setActive(true);
    }

    @Override
    public Dimension getPreferedSize() {
        return new Dimension(getParent().getBounds().width, 15);
    }

    private class ChannelClickListener extends GuiMouseAdapter {
        @Override
        public void mouseClicked(GuiMouseEvent event) {
            ChatTab comp = (ChatTab) event.getComponent();
            if (event.getButton() == 0) {
                if (GuiScreen.isShiftKeyDown()) {
                    // Remove channel
                    TabbyChat.getInstance().getChat().removeChannel(comp.getChannel());
                } else if (GuiScreen.isCtrlKeyDown()) {
                    // Toggle channel
                    boolean active = comp.getChannel().isActive();
                    comp.getChannel().setActive(!active);
                } else {
                    // Enable channel, disable others
                    for (Channel chan : TabbyAPI.getAPI().getChat().getChannels()) {
                        chan.setActive(false);
                    }
                    comp.getChannel().setActive(true);
                }

            } else if (event.getButton() == 1) {
                // Open channel options
                comp.getChannel().openSettings();
            }
        }
    }
}
