package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.api.TabbyAPI;
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

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;

public class ChatArea extends GuiComponent implements Supplier<List<Message>>, GuiMouseAdapter {

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
            int div = 60;
            if (GuiScreen.isShiftKeyDown()) {
                div *= 3;
            }
            scroll(scroll / div);
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
        // TODO Setting
        int div = GuiNewChatTC.getInstance().getChatOpen() ? 1 : 2;
        while (pos < lines.size() && length < getBounds().height / div - 8) {
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
            double opacPerc = age / TabbyChat.getInstance().chatSettings.fadeTime.getValue();
            opacPerc = 1.0D - opacPerc;
            opacPerc *= 10.0D;

            opacPerc = Math.max(0, opacPerc);
            opacPerc = Math.min(1, opacPerc);

            opacPerc *= opacPerc;
            opacity = (int) (opacity * opacPerc);
        }
        return opacity;
    }

    public void scroll(int scr) {
        setScrollPos(getScrollPos() + scr);
    }

    public void setScrollPos(int scroll) {
        List<Message> list = getChat(false);
        scroll = Math.min(scroll, list.size() - (getBounds().height / mc.fontRendererObj.FONT_HEIGHT));
        scroll = Math.max(scroll, 0);

        this.scrollPos = scroll;
    }

    public int getScrollPos() {
        return scrollPos;
    }

    public void resetScroll() {
        setScrollPos(0);
    }

    public IChatComponent getChatComponent(int clickX, int clickY) {
        IChatComponent result = null;
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            if (clickX >= getActualPosition().x && clickY >= getActualPosition().y
                    && clickX <= getActualPosition().x + getBounds().width
                    && clickY <= getActualPosition().y + getBounds().height) {

                int bottom = getActualPosition().y + getBounds().height;
                int linePos = Math
                        .abs((clickY - getBounds().height - getActualPosition().y - (bottom % mc.fontRendererObj.FONT_HEIGHT))
                                / (this.mc.fontRendererObj.FONT_HEIGHT) + 1);

                if (linePos >= 0 && linePos < this.getVisibleChat().size()) {
                    Message chatline = getVisibleChat().get(linePos);
                    int l1 = 0;
                    Iterator<IChatComponent> iterator = chatline.getMessageWithOptionalTimestamp().iterator();

                    while (iterator.hasNext() && l1 <= clickX) {
                        IChatComponent ichatcomponent = iterator.next();

                        if (ichatcomponent instanceof ChatComponentText) {
                            l1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(
                                    ((ChatComponentText) ichatcomponent).getChatComponentText_TextValue(), true));
                            result = ichatcomponent;
                        }
                    }
                }
            }
        }
        return result;
    }

    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

}
