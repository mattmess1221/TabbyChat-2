package mnm.mods.tabbychat.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.client.AbstractChannel;
import mnm.mods.tabbychat.client.ChatManager;
import mnm.mods.tabbychat.client.ChatMessage;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;
import mnm.mods.tabbychat.util.ChatTextUtils;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.LocalVisibility;
import mnm.mods.tabbychat.util.TexturedModal;
import net.minecraft.client.gui.RenderComponentsUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.ChatVisibility;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ChatArea extends GuiComponent {

    private static final TexturedModal MODAL = new TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 205);


    private AbstractChannel channel;
    private int scrollPos = 0;

    public ChatArea() {
        this.setMinimumSize(new Dim(300, 160));
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        // One tick = 120
        if (getLocation().contains(x, y) && scroll != 0) {
            if (scroll > 1) {
                scroll = 1;
            }
            if (scroll < -1) {
                scroll = -1;
            }
            if (Screen.hasShiftDown()) {
                scroll *= 7;
            }
            scroll((int) scroll);
            return true;
        }
        return false;
    }

    @Override
    public void onClosed() {
        resetScroll();
        super.onClosed();
    }

    @Override
    public ILocation getLocation() {
        List<ChatMessage> visible = getVisibleChat();
        int height = visible.size() * mc.fontRenderer.FONT_HEIGHT;
        LocalVisibility vis = TabbyChatClient.getInstance().getSettings().advanced.visibility.get();

        if (mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen() || vis == LocalVisibility.ALWAYS) {
            return super.getLocation();
        } else if (height != 0) {
            int y = super.getLocation().getHeight() - height;
            return super.getLocation().copy().move(0, y - 2).setHeight(height + 2);
        }
        return super.getLocation();
    }

    @Override
    public boolean isVisible() {

        List<ChatMessage> visible = getVisibleChat();
        int height = visible.size() * mc.fontRenderer.FONT_HEIGHT;
        LocalVisibility vis = TabbyChatClient.getInstance().getSettings().advanced.visibility.get();

        return mc.gameSettings.field_74343_n/*chatVisibility*/ != ChatVisibility.HIDDEN
                && (mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen() || vis == LocalVisibility.ALWAYS || height != 0);
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {

        List<ChatMessage> visible = getVisibleChat();
        GlStateManager.enableBlend();
        float opac = (float) mc.gameSettings.chatOpacity;
        GlStateManager.color4f(1, 1, 1, opac);

        drawModalCorners(MODAL);

        blitOffset = 100;
        // TODO abstracted padding
        int xPos = getLocation().getXPos() + 3;
        int yPos = getLocation().getYHeight();
        for (ChatMessage line : visible) {
            yPos -= mc.fontRenderer.FONT_HEIGHT;
            drawChatLine(line, xPos, yPos);
        }
        blitOffset = 0;
        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();
    }

    private void drawChatLine(ChatMessage line, int xPos, int yPos) {
        String text = ChatTextUtils.getMessageWithOptionalTimestamp(line).getFormattedText();
        mc.fontRenderer.drawStringWithShadow(text, xPos, yPos, Color.WHITE.getHex() + (getLineOpacity(line) << 24));
    }

    public void setChannel(AbstractChannel channel) {
        this.channel = channel;
//        this.markDirty();
    }

    @Deprecated
    public void markDirty() {
        ChatManager.instance().markDirty(channel);
    }

    List<ChatMessage> getChat() {
        return ChatManager.instance().getVisible(channel, super.getLocation().getWidth() - 6);
    }

    private List<ChatMessage> getVisibleChat() {
        List<ChatMessage> lines = getChat();

        List<ChatMessage> messages = new ArrayList<>();
        int length = 0;

        int pos = getScrollPos();
        float unfoc = TabbyChatClient.getInstance().getSettings().advanced.unfocHeight.get();
        float div = mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen() ? 1 : unfoc;
        while (pos < lines.size() && length < super.getLocation().getHeight() * div - 10) {
            ChatMessage line = lines.get(pos);

            if (mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen()) {
                messages.add(line);
            } else if (getLineOpacity(line) > 3) {
                messages.add(line);
            } else {
                break;
            }

            pos++;
            length += mc.fontRenderer.FONT_HEIGHT;
        }

        return messages;
    }

    private int getLineOpacity(ChatMessage line) {
        LocalVisibility vis = TabbyChatClient.getInstance().getSettings().advanced.visibility.get();
        if (vis == LocalVisibility.ALWAYS)
            return 4;
        if (vis == LocalVisibility.HIDDEN && !mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen())
            return 0;
        int opacity = (int) (mc.gameSettings.chatOpacity * 255);

        double age = mc.field_71456_v/*ingameGUI*/.getTicks() - line.getCounter();
        if (!mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen()) {
            double opacPerc = age / TabbyChatClient.getInstance().getSettings().advanced.fadeTime.get();
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
        List<ChatMessage> list = getChat();
        scroll = Math.min(scroll, list.size() - mc.field_71456_v/*ingameGUI*/.getChatGUI().getLineCount());
        scroll = Math.max(scroll, 0);

        this.scrollPos = scroll;
    }

    public int getScrollPos() {
        return scrollPos;
    }

    public void resetScroll() {
        setScrollPos(0);
    }

    @Nullable
    public ITextComponent getTextComponent(int clickX, int clickY) {
        if (mc.field_71456_v/*ingameGUI*/.getChatGUI().getChatOpen()) {
            double scale = mc.field_71456_v/*ingameGUI*/.getChatGUI().getScale();
            clickX = MathHelper.floor(clickX / scale);
            clickY = MathHelper.floor(clickY / scale);

            ILocation actual = getLocation();
            // check that cursor is in bounds.
            if (actual.contains(clickX, clickY)) {


                double size = mc.fontRenderer.FONT_HEIGHT * scale;
                double bottom = (actual.getYPos() + actual.getHeight());

                // The line to get
                int linePos = MathHelper.floor((clickY - bottom) / -size) + scrollPos;

                // Iterate through the chat component, stopping when the desired
                // x is reached.
                List<ChatMessage> list = this.getChat();
                if (linePos >= 0 && linePos < list.size()) {
                    ChatMessage chatline = list.get(linePos);
                    float x = actual.getXPos() + 3;

                    for (ITextComponent ichatcomponent : ChatTextUtils.getMessageWithOptionalTimestamp(chatline)) {
                        if (ichatcomponent instanceof StringTextComponent) {

                            // get the text of the component, no children.
                            String text = ichatcomponent.getUnformattedComponentText();
                            // clean it up
                            String clean = RenderComponentsUtil.removeTextColorsIfConfigured(text, false);
                            // get it's width, then scale it.
                            x += this.mc.fontRenderer.getStringWidth(clean) * scale;

                            if (x > clickX) {
                                return ichatcomponent;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
