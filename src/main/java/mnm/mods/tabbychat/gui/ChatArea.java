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
import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.gui.ReceivedChat;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatTextUtils;
import mnm.mods.tabbychat.util.ChatVisibility;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.ILocation;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class ChatArea extends GuiComponent implements Supplier<List<Message>>, ReceivedChat {

    private Supplier<List<Message>> supplier = Suppliers.memoizeWithExpiration(this, 50, TimeUnit.MILLISECONDS);
    private int scrollPos = 0;

    public ChatArea() {
        this.setMinimumSize(new Dimension(300, 160));
    }

    @Subscribe
    public void superScrollingAction(GuiMouseEvent event) {
        if (event.getType() == MouseEvent.SCROLL) {
            // Scrolling
            int scroll = event.getScroll();
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
            int fore = getForeColor().getHex();
            int back = getBackColor().getHex();
            List<Message> visible = getVisibleChat();
            int height = visible.size() * mc.fontRendererObj.FONT_HEIGHT;
            ChatVisibility vis = TabbyChat.getInstance().settings.advanced.visibility.get();
            if (GuiNewChatTC.getInstance().getChatOpen()) {
                Gui.drawRect(0, 0, getBounds().width, getBounds().height, back);
                this.drawVerticalLine(-1, -1, getBounds().height, fore);
                this.drawVerticalLine(getBounds().width, -1, getBounds().height, fore);
            } else if (vis == ChatVisibility.ALWAYS) {
                Gui.drawRect(0, 0, getBounds().width, getBounds().height, back);
                drawBorders(0, 0, getBounds().width, getBounds().height, fore);
            } else if (height != 0) {
                int y = getBounds().height - height;
                Gui.drawRect(getBounds().x, y - 1, getBounds().width, y + height, back);
                drawBorders(getBounds().x, y - 1, getBounds().width, y + height, fore);
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
        Color color = TabbyChat.getInstance().settings.colors.chatTextColor.get();
        mc.fontRendererObj.drawStringWithShadow(text, xPos, yPos, (color.getHex()) + (getLineOpacity(line) << 24));
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public List<Message> get() {
        return getChat(true);
    }

    private List<Message> getChat(boolean force) {
        if (!force) {
            return supplier.get();
        }
        Channel channel = TabbyAPI.getAPI().getChat().getActiveChannel();
        return ChatTextUtils.split(channel.getMessages(), getBounds().width);

    }

    public List<Message> getChat() {
        return getChat(false);
    }

    public List<Message> getVisibleChat() {
        List<Message> lines = getChat();

        List<Message> messages = Lists.newArrayList();
        int length = 0;

        int pos = getScrollPos();
        float unfoc = TabbyChat.getInstance().settings.advanced.unfocHeight.get();
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
        ChatVisibility vis = TabbyChat.getInstance().settings.advanced.visibility.get();
        if (vis == ChatVisibility.ALWAYS)
            return 4;
        if (vis == ChatVisibility.HIDDEN && !GuiNewChatTC.getInstance().getChatOpen())
            return 0;
        int opacity = TabbyChat.getInstance().settings.colors.chatTextColor.get().getAlpha();

        double age = mc.ingameGUI.getUpdateCounter() - line.getCounter();
        if (!mc.ingameGUI.getChatGUI().getChatOpen()) {
            double opacPerc = age / TabbyChat.getInstance().settings.advanced.fadeTime.get();
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
        List<Message> list = getChat();
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
    public ITextComponent getChatComponent(int clickX, int clickY) {
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            Point point = scalePoint(new Point(clickX, clickY));
            ILocation actual = getActualLocation();
            // check that cursor is in bounds.
            if (point.x >= actual.getXPos() && point.y >= actual.getYPos()
                    && point.x <= actual.getXPos() + actual.getWidth()
                    && point.y <= actual.getYPos() + actual.getHeight()) {

                float scale = getActualScale();
                float size = mc.fontRendererObj.FONT_HEIGHT * scale;
                float bottom = (actual.getYPos() + actual.getHeight());

                // The line to get
                int linePos = MathHelper.floor_float((point.y - bottom) / -size) + scrollPos;

                // Iterate through the chat component, stopping when the desired
                // x is reached.
                List<Message> list = this.getChat();
                if (linePos >= 0 && linePos < list.size()) {
                    Message chatline = list.get(linePos);
                    float x = actual.getXPos();
                    Iterator<ITextComponent> iterator = chatline.getMessageWithOptionalTimestamp().iterator();

                    while (iterator.hasNext()) {
                        ITextComponent ichatcomponent = iterator.next();

                        if (ichatcomponent instanceof TextComponentString) {

                            // get the text of the component, no children.
                            String text = ichatcomponent.getUnformattedComponentText();
                            // clean it up
                            String clean = GuiUtilRenderComponents.removeTextColorsIfConfigured(text, false);
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
    public Rectangle getBounds() {
        return getLocation().asRectangle();
    }
}
