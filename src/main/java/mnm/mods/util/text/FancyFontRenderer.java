package mnm.mods.util.text;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.text.ITextComponent;

public class FancyFontRenderer extends Gui {

    private final FontRenderer fontRenderer;

    public FancyFontRenderer(FontRenderer fr) {
        this.fontRenderer = fr;
    }

    public void drawChat(ITextComponent chat, int x, int y) {
        this.drawChat(chat, x, y, true);
    }

    public void drawChat(ITextComponent chat, int x, int y, boolean shadow) {
        drawChat(chat, x, y, -1, shadow);
    }

    public void drawChat(ITextComponent chat, int x, int y, int color) {
        this.drawChat(chat, x, y, color, true);
    }

    public void drawChat(ITextComponent chat, int x, int y, int color, boolean shadow) {

        int x1 = x;
        for (ITextComponent c : chat) {
            if (c instanceof FancyTextComponent) {
                FancyTextComponent fcc = (FancyTextComponent) c;
                for (String s : c.getUnformattedText().split("\r?\n")) {
                    int length = fontRenderer.getStringWidth(s);
                    drawRect(x1, y, x1 + length, y - fontRenderer.FONT_HEIGHT, fcc.getFancyStyle().getHighlight().getHex());
                    drawHorizontalLine(x1, x1 + length, y + fontRenderer.FONT_HEIGHT - 1, fcc.getFancyStyle().getUnderline().getHex());
                }
            }
            x1 += fontRenderer.getStringWidth(c.getUnformattedComponentText());
        }
        for (String s : chat.getUnformattedText().split("\r?\n")) {
            fontRenderer.drawString(s, x, y, color, shadow);
            y += fontRenderer.FONT_HEIGHT;
        }
    }

}
