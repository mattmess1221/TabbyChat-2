package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Message;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.util.ChatMessage;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseWheelEvent;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Lists;

public class ChatArea extends GuiComponent {

    private List<Message> chatLines = Lists.newArrayList();
    private int scrollPos = 0;

    public ChatArea() {
        this.addEventListener(new GuiMouseAdapter() {
            @Override
            public void mouseWheelMoved(GuiMouseWheelEvent event) {
                // Scrolling
                int scroll = event.getWheelDirection();
                // One tick = 120
                int div = 60;
                if (GuiScreen.isShiftKeyDown()) {
                    div *= 3;
                }
                scroll(scroll / div);
            }
        });
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (mc.gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
            List<Message> visible = getVisibleChat();
            int height = visible.size() * mc.fontRendererObj.FONT_HEIGHT;
            if (GuiNewChatTC.getInstance().getChatOpen()) {
                Gui.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height,
                        getBackColor());
                drawBorders(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
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
        String text = line.getMessage().getFormattedText();
        mc.fontRendererObj.func_175063_a(text, xPos, yPos, (getForeColor())
                + (getLineOpacity(line) << 24));
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    /**
     * Adds a message.
     *
     * @param line The message
     */
    public void addChatMessage(IChatComponent line) {
        addChatMessage(line, 0);
    }

    /**
     * Adds a message that may be deleted. The id determines whether to delete
     * it. If a message is sent with the same id as another (except 0), all
     * other messages with that id will be removed.
     *
     * @param chat The message
     * @param id The id of the message.
     */
    public void addChatMessage(IChatComponent chat, int id) {
        if (id != 0) {
            removeChatLines(id);
        }
        int counter = mc.ingameGUI.getUpdateCounter();
        Message line = new ChatMessage(counter, chat, id);
        chatLines.add(line);
        while (chatLines.size() > 100) {
            chatLines.remove(chatLines.size() - 1);
        }
    }

    /**
     * Removes any message with the given id.
     *
     * @param id The ID of the message
     */
    public void removeChatLines(int id) {
        Iterator<Message> iter = this.chatLines.iterator();

        while (iter.hasNext()) {
            Message line = iter.next();
            if (line.getID() == id) {
                iter.remove();
            }
        }
    }

    /**
     * Removes a message that the given position.
     *
     * @param pos The position of the message.
     */
    public void removeChatMessage(int pos) {
        if (chatLines.size() > pos) {
            chatLines.remove(chatLines.size() - pos);
        }
    }

    public void clearMessages() {
        chatLines.clear();
    }

    public void addChatLine(Message line) {
        chatLines.add(0, line);
    }

    private List<Message> filterDeactiveChannels(List<Message> messages) {
        List<Message> result = Lists.newArrayList();
        for (Message mess : messages) {
            if (mess.isActive()) {
                result.add(mess);
            }
        }
        return result;

    }

    private List<Message> getVisibleChat() {
        List<Message> visible = Lists.newArrayList();
        int length = 0;
        List<Message> lines = filterDeactiveChannels(chatLines);
        int pos = scrollPos;
        // TODO Setting
        int div = GuiNewChatTC.getInstance().getChatOpen() ? 1 : 2;
        while (pos < lines.size() && length < getBounds().height / div - 8) {
            Message line = lines.get(pos);

            if (GuiNewChatTC.getInstance().getChatOpen()) {
                visible.add(line);
            } else if (getLineOpacity(line) > 3) {
                visible.add(line);
            } else {
                break;
            }

            pos++;
            length += mc.fontRendererObj.FONT_HEIGHT;
        }

        return visible;
    }

    private int getLineOpacity(Message line) {
        int opacity = (int) (255 * mc.gameSettings.chatOpacity);
        double age = mc.ingameGUI.getUpdateCounter() - line.getCounter();
        if (!mc.ingameGUI.getChatGUI().getChatOpen()) {
            double opacPerc = age / TabbyChat.getInstance().chatSettings.fadeTime.getValue();
            opacPerc = 1.0D - opacPerc;
            opacPerc *= 10.0D;

            opacPerc = Math.max(0, opacPerc);
            opacPerc = Math.min(1, opacPerc);

            opacPerc *= opacPerc;
            opacity = (int) (255.0D * opacPerc * mc.gameSettings.chatOpacity);
        }
        return opacity;
    }

    public void scroll(int scr) {
        this.scrollPos += scr;
        this.scrollPos = Math.min(this.scrollPos, this.chatLines.size()
                - (getBounds().height / mc.fontRendererObj.FONT_HEIGHT));
        this.scrollPos = Math.max(this.scrollPos, 0);
    }

    public void resetScroll() {
        this.scrollPos = 0;

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
                    @SuppressWarnings("unchecked")
                    Iterator<IChatComponent> iterator = chatline.getMessage().iterator();

                    while (iterator.hasNext() && l1 <= clickX) {
                        IChatComponent ichatcomponent = iterator.next();

                        if (ichatcomponent instanceof ChatComponentText) {
                            l1 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents
                                    .func_178909_a(((ChatComponentText) ichatcomponent)
                                            .getChatComponentText_TextValue(), true));
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

    @Override
    public Dimension getPreferedSize() {
        return new Dimension(300, 160);
    }

}
