package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.gui.ReceivedChat;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatTextUtils;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

public class ChatArea extends GuiComponent implements Supplier<List<Message>>, GuiMouseAdapter, ReceivedChat<GuiComponent> {

    private Supplier<List<Message>> supplier = Suppliers.memoizeWithExpiration(this, 50, TimeUnit.MILLISECONDS);
    private int scrollPos = 0;

    public ChatArea() {
        this.setMinimumSize(new Dimension(300, 160));
    }

    @Override
    public void accept(GuiMouseEvent event) {
        if (event.event == GuiMouseEvent.SCROLLED) {
            // Scrolling
            int scroll = event.scroll;
            // One tick = 120
            if (scroll != 0) {
                if (scroll > 1) {
                    scroll = 1;
                }
                if (scroll < -1) {
                    scroll = -1;
                }
                if (GuiScreen.isShiftKeyDown()) {
                    scroll *= 7;
                }
                scroll(scroll);

            }
        }
    }

    @Override
    public void onClosed() {
        resetScroll();
        super.onClosed();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (mc.gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
            List<Message> visible = getVisibleChat();
            int height = visible.size() * mc.fontRendererObj.FONT_HEIGHT;
            if (GuiNewChatTC.getInstance().getChatOpen()) {
                Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
                drawBorders(0, 0, getBounds().width, getBounds().height);
            } else if (height != 0) {
                int y = getBounds().height - height;
                Gui.drawRect(getBounds().x, y - 1, getBounds().width, y + height, getBackColor());
                drawBorders(getBounds().x, y - 1, getBounds().width, y + height);
            }
            int xPos = getBounds().x + 1;
            int yPos = getBounds().height;
            for (Message line : visible) {
                yPos -= mc.fontRendererObj.FONT_HEIGHT;
                drawChatLine(line, xPos, yPos);
            }
        }
        super.drawComponent(mouseX, mouseY);
    }

    private void drawChatLine(Message line, int xPos, int yPos) {
        GlStateManager.enableBlend();
        String text = line.getMessageWithOptionalTimestamp().getFormattedText();
        mc.fontRendererObj.drawStringWithShadow(text, xPos, yPos, (getForeColor()) + (getLineOpacity(line) << 24));
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public List<Message> get() {
        return getChat(true);
    }

    public List<Message> getChat(boolean force) {
        if (!force) {
            return supplier.get();
        }
        return getChat();
    }

    private List<Message> getChat() {
        Channel channel = TabbyAPI.getAPI().getChat().getActiveChannel();
        return ChatTextUtils.split(channel.getMessages(), getBounds().width);
    }

    public List<Message> getVisibleChat() {
        List<Message> lines = getChat(false);

        List<Message> messages = Lists.newArrayList();
        int length = 0;

        int pos = getScrollPos();
        float unfoc = TabbyChat.getInstance().settings.advanced.unfocHeight.getValue();
        float div = GuiNewChatTC.getInstance().getChatOpen() ? 1 : unfoc;
        while (pos < lines.size() && length < getBounds().height * div - 8) {
            Message line = lines.get(pos);

            if (GuiNewChatTC.getInstance().getChatOpen()) {
                messages.add(line);
            } else if (getLineOpacity(line) > 3) {
                messages.add(line);
            } else {
                break;
            }

            pos++;
            length += mc.fontRendererObj.FONT_HEIGHT;
        }

        return messages;
    }

    private int getLineOpacity(Message line) {
        int opacity = new Color(getForeColor()).getAlpha();
        double age = mc.ingameGUI.getUpdateCounter() - line.getCounter();
        if (!mc.ingameGUI.getChatGUI().getChatOpen()) {
            double opacPerc = age / TabbyChat.getInstance().settings.advanced.fadeTime.getValue();
            opacPerc = 1.0D - opacPerc;
            opacPerc *= 10.0D;

            opacPerc = Math.max(0, opacPerc);
            opacPerc = Math.min(1, opacPerc);

            opacPerc *= opacPerc;
            opacity = (int) (opacity * opacPerc);
        }
        return opacity;
    }

    @Override
    public void scroll(int scr) {
        setScrollPos(getScrollPos() + scr);
    }

    @Override
    public void setScrollPos(int scroll) {
        List<Message> list = getChat(false);
        scroll = Math.min(scroll, list.size() - GuiNewChatTC.getInstance().getLineCount());
        scroll = Math.max(scroll, 0);

        this.scrollPos = scroll;
    }

    @Override
    public int getScrollPos() {
        return scrollPos;
    }

    @Override
    public void resetScroll() {
        setScrollPos(0);
    }

    @Override
    public IChatComponent getChatComponent(int clickX, int clickY) {
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            Point point = scalePoint(new Point(clickX, clickY));
            Rectangle actual = getActualBounds();
            // check that cursor is in bounds.
            if (point.x >= actual.x && point.y >= actual.y
                    && point.x <= actual.x + actual.width
                    && point.y <= actual.y + actual.height) {

                float scale = getActualScale();
                float size = mc.fontRendererObj.FONT_HEIGHT * scale;
                float bottom = (actual.y + actual.height);

                // The line to get
                int linePos = MathHelper.floor_float((point.y - bottom) / -size) + scrollPos;

                // Iterate through the chat component, stopping when the desired
                // x is reached.
                List<Message> list = this.getVisibleChat();
                if (linePos >= 0 && linePos < list.size()) {
                    Message chatline = list.get(linePos);
                    float x = actual.x;
                    Iterator<IChatComponent> iterator = chatline.getMessageWithOptionalTimestamp().iterator();

                    while (iterator.hasNext()) {
                        IChatComponent ichatcomponent = iterator.next();

                        if (ichatcomponent instanceof ChatComponentText) {

                            // get the text of the component, no children.
                            String text = ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue();
                            // clean it up
                            String clean = GuiUtilRenderComponents.func_178909_a(text, false);
                            // get it's width, then scale it.
                            x += this.mc.fontRendererObj.getStringWidth(clean) * scale;

                            if (x > point.x) {
                                return ichatcomponent;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public GuiComponent asGui() {
        return this;
    }
}
