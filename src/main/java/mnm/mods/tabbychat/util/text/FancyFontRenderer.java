package mnm.mods.tabbychat.util.text;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.ITextComponent;

public class FancyFontRenderer extends Gui {

    private final FontRenderer fontRenderer;

    public FancyFontRenderer(FontRenderer fr) {
        this.fontRenderer = fr;
    }

    public void drawChat(ITextComponent chat, float x, float y) {
        this.drawChat(chat, x, y, true);
    }

    public void drawChat(ITextComponent chat, float x, float y, boolean shadow) {
        drawChat(chat, x, y, -1, shadow);
    }

    public void drawChat(ITextComponent chat, float x, float y, int color) {
        this.drawChat(chat, x, y, color, true);
    }

    public void drawChat(ITextComponent chat, float x, float y, int color, boolean shadow) {

        float x1 = x;
        for (ITextComponent c : chat) {
            if (c instanceof FancyTextComponent) {
                FancyTextComponent fcc = (FancyTextComponent) c;
                for (String s : c.getString().split("\r?\n")) {
                    int length = fontRenderer.getStringWidth(s);
                    drawRect((int) x1, (int) y, (int) x1 + length, (int) y - fontRenderer.FONT_HEIGHT, fcc.getFancyStyle().getHighlight().getHex());
                    drawHorizontalLine((int) x1, (int) x1 + length, (int) y + fontRenderer.FONT_HEIGHT - 1, fcc.getFancyStyle().getUnderline().getHex());
                }
            }
            x1 += fontRenderer.getStringWidth(c.getUnformattedComponentText());
        }
        for (String s : chat.getString().split("\r?\n")) {
            if (shadow) {
                fontRenderer.drawStringWithShadow(s, x, y, color);
            }else {
                fontRenderer.drawString(s, x, y, color);
            }
            y += fontRenderer.FONT_HEIGHT;
        }
    }

}
