package mnm.mods.tabbychat.gui;

import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.extra.spell.Spellcheck;
import mnm.mods.tabbychat.extra.spell.SpellingFormatter;
import mnm.mods.util.Color;
import mnm.mods.util.Dim;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.text.FancyFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IGuiEventListenerDeferred;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TextBox extends GuiComponent implements IGuiEventListenerDeferred {

    private static final TexturedModal MODAL = new TexturedModal(ChatBox.GUI_LOCATION, 0, 219, 254, 37);

    private FontRenderer fr = mc.fontRenderer;
    // Dummy textField
    private GuiText textField = new GuiText(new GuiTextField(0, fr, 0, 0, 0, 0) {
        @Override
        public void drawTextField(int x, int y, float parTicks) {
            // noop
        }
    });
    private int cursorCounter;
    private Spellcheck spellcheck;

    TextBox() {
        this.spellcheck = TabbyChatClient.getInstance().getSpellcheck();

        textField.getTextField().setMaxStringLength(ChatManager.MAX_CHAT_LENGTH);
        textField.getTextField().setCanLoseFocus(false);
        textField.getTextField().setEnableBackgroundDrawing(false);
        textField.getTextField().setFocused(true);
    }

    @Nullable
    @Override
    public IGuiEventListener getFocused() {
        return textField;
    }

    @Override
    public void onClosed() {
        this.textField.setValue("");
        super.onClosed();
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        GlStateManager.enableBlend();
        drawModalCorners(MODAL);
        GlStateManager.disableBlend();

        drawText();
        drawCursor();

    }

    private void drawCursor() {
        GuiTextField textField = this.textField.getTextField();

        // keeps track of all the characters. Used to compensate for spaces
        int totalPos = 0;

        // The current pixel row. adds FONT_HEIGHT each iteration
        int line = 0;

        // The position of the cursor
        int pos = textField.getCursorPosition();
        // the position of the selection
        int sel = textField.getSelectionEnd();

        // make the position and selection in order
        int start = Math.min(pos, sel);
        int end = Math.max(pos, sel);

        ILocation loc = getLocation();

        for (String text : getWrappedLines()) {

            // cursor drawing
            if (pos >= 0 && pos <= text.length()) {
                // cursor is on this line
                int c = fr.getStringWidth(text.substring(0, pos));
                boolean cursorBlink = this.cursorCounter / 6 % 3 != 0;
                if (cursorBlink) {
                    if (textField.getCursorPosition() < this.textField.getValue().length()) {
                        drawVerticalLine(loc.getXPos() + c + 3,
                                loc.getYPos() + line - 2,
                                loc.getYPos() + line + fr.FONT_HEIGHT + 1, 0xffd0d0d0);
                    } else {
                        fr.drawString("_", loc.getXPos() + c + 2, loc.getYPos() + line + 1, getPrimaryColorProperty().getHex());
                    }

                }
            }

            // selection highlighting

            // the start of the highlight.
            int x = -1;
            // the end of the highlight.
            int w = -1;

            // test the start
            if (start >= 0 && start <= text.length()) {
                x = fr.getStringWidth(text.substring(0, start));
            }

            // test the end
            if (end >= 0 && end <= text.length()) {
                w = fr.getStringWidth(text.substring(start < 0 ? 0 : start, end)) + 2;
            }

            final int LINE_Y = line + fr.FONT_HEIGHT + 2;

            if (w != 0) {
                if (x >= 0 && w > 0) {
                    // start and end on same line
                    drawSelectionBox(x + 2, line, x + w, LINE_Y);
                } else {
                    if (x >= 0) {
                        // started on this line
                        drawSelectionBox(x + 2, line, x + fr.getStringWidth(text.substring(start)) + 1, LINE_Y);
                    }
                    if (w >= 0) {
                        // ends on this line
                        drawSelectionBox(2, line, w, LINE_Y);
                    }
                    if (start < 0 && end > text.length()) {
                        // full line
                        drawSelectionBox(1, line, fr.getStringWidth(text), LINE_Y);
                    }
                }
            }

            // keep track of the lines
            totalPos += text.length();
            boolean space = getText().length() > totalPos && getText().charAt(totalPos) == ' ';

            // prepare all the markers for the next line.
            pos -= text.length();
            start -= text.length();
            end -= text.length();

            if (space) {
                // compensate for spaces
                pos--;
                start--;
                end--;
                totalPos++;
            }
            line = LINE_Y;
        }

    }

    private void drawText() {
        FancyFontRenderer ffr = new FancyFontRenderer(fr);
        int yPos = 1;
        List<ITextComponent> lines = getFormattedLines();
        ILocation loc = getLocation();
        for (ITextComponent line : lines) {
            Color color = Color.WHITE;
            ffr.drawChat(line, loc.getXPos() + 3, loc.getYPos() + yPos, color.getHex(), false);
            yPos += fr.FONT_HEIGHT + 2;
        }

    }

    /**
     * Draws the blue selection box. Adapted from {@code GuiTextField#drawSelectionBox(int, int, int, int)}
     */
    private void drawSelectionBox(int x1, int y1, int x2, int y2) {
        ILocation loc = getLocation();
        x1 += loc.getXPos();
        x2 += loc.getXPos();
        y1 += loc.getYPos();
        y2 += loc.getYPos();

        if (x1 < x2) {
            int i = x1;
            x1 = x2;
            x2 = i;
        }

        if (y1 < y2) {
            int j = y1;
            y1 = y2;
            y2 = j;
        }

        x2 = Math.min(x2, this.getLocation().getXWidth());
        x1 = Math.min(x1, this.getLocation().getXWidth());

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color4f(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(x1, y2, 0.0D).endVertex();
        bufferbuilder.pos(x2, y2, 0.0D).endVertex();
        bufferbuilder.pos(x2, y1, 0.0D).endVertex();
        bufferbuilder.pos(x1, y1, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }


    @Override
    public void tick() {
        this.cursorCounter++;
    }

    private List<String> getWrappedLines() {
        return fr.listFormattedStringToWidth(textField.getValue(), getLocation().getWidth());
    }

    private List<ITextComponent> getFormattedLines() {
        List<String> lines = getWrappedLines();
        if (TabbyChatClient.getInstance().getSettings().advanced.spelling.get()) {
            spellcheck.checkSpelling(textField.getValue());
            return lines.stream()
                    .map(new SpellingFormatter(spellcheck))
                    .collect(Collectors.toList());
        }
        return lines.stream()
                .map(TextComponentString::new)
                .collect(Collectors.toList());
    }

    @Override
    @Nonnull
    public Dim getMinimumSize() {
        return new Dim(100, (fr.FONT_HEIGHT + 2) * getWrappedLines().size());
    }

    public GuiText getTextField() {
        return textField;
    }

    public String getText() {
        return textField.getValue();
    }

    public void setText(String text) {
        textField.setValue(text);
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        if (mouseButton == 0) {
            ILocation bounds = this.getLocation();

            int width = bounds.getWidth() - 1;
            int row = (int) y / (fr.FONT_HEIGHT + 2);

            List<String> lines = getWrappedLines();
            if (row < 0 || row >= lines.size() || x < 0 || x > width) {
                return false;
            }
            int index = 0;
            for (int i = 0; i < row; i++) {
                index += lines.get(i).length();
                // check for spaces because trailing spaces are trimmed
                if (getText().charAt(index)==' ') {
                    index++;
                }
            }
            index += fr.trimStringToWidth(lines.get(row), (int) x - 3).length();
            textField.getTextField().setCursorPosition(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && mc.ingameGUI.getChatGUI().getChatOpen();
    }
}
