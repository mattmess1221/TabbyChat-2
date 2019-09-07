package mnm.mods.tabbychat.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.client.ChatManager;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.extra.spell.Spellcheck;
import mnm.mods.tabbychat.client.extra.spell.SpellingFormatter;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;
import mnm.mods.tabbychat.client.gui.component.GuiText;
import mnm.mods.tabbychat.client.gui.component.IGuiEventListenerDelegate;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.TexturedModal;
import mnm.mods.tabbychat.util.text.FancyFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class TextBox extends GuiComponent implements IGuiEventListenerDelegate {

    private static final TexturedModal MODAL = new TexturedModal(ChatBox.GUI_LOCATION, 0, 219, 254, 37);

    private FontRenderer fr = mc.fontRenderer;
    // Dummy textField
    private GuiText textField = new GuiText(new TextFieldWidget(fr, 0, 0, 0, 0, "input") {
        @Override
        public void render(int x, int y, float parTicks) {
            // noop
        }

        @Override
        public void setSuggestion(@Nullable String p_195612_1_) {
            suggestion = p_195612_1_;
            super.setSuggestion(p_195612_1_);
        }
    });
    private int cursorCounter;
    private Spellcheck spellcheck;

    private BiFunction<String, Integer, String> textFormatter = (text, offset) -> text;
    private String suggestion;

    TextBox() {
        this.spellcheck = TabbyChatClient.getInstance().getSpellcheck();

        textField.getTextField().setMaxStringLength(ChatManager.MAX_CHAT_LENGTH);
        textField.getTextField().setCanLoseFocus(false);
        textField.getTextField().setEnableBackgroundDrawing(false);
        textField.getTextField().setFocused2(true);
    }

    @Override
    public IGuiEventListener delegate() {
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
        TextFieldWidget textField = this.textField.getTextField();

        // keeps track of all the characters. Used to compensate for spaces
        int totalPos = 0;

        // The current pixel row. adds FONT_HEIGHT each iteration
        int line = 0;

        // The position of the cursor
        int pos = textField.getCursorPosition();
        // the position of the selection
        int sel = pos + textField.getSelectedText().length();

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
                        vLine(loc.getXPos() + c + 3,
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
        ILocation loc = getLocation();
        int xPos = loc.getXPos() + 3;
        int yPos = loc.getYPos() + 1;
        List<ITextComponent> lines = getFormattedLines();
        for (ITextComponent line : lines) {
            Color color = Color.WHITE;
            xPos = loc.getXPos() + 3;
            ffr.drawChat(line, xPos, yPos, color.getHex(), false);
            yPos += fr.FONT_HEIGHT + 2;
            xPos += fr.getStringWidth(line.getString());
        }
        yPos -= fr.FONT_HEIGHT + 2;

        boolean flag2 = textField.getTextField().getCursorPosition() < getText().length() || getText().length() >= textField.getTextField().getMaxStringLength();

        int x = loc.getXPos() + 3;
        if (!flag2 && suggestion != null) {
            this.fr.drawStringWithShadow(this.suggestion, xPos, yPos, -8355712);
        }

    }

    /**
     * Draws the blue selection box. Forwards to {@link TextFieldWidget#drawSelectionBox(int, int, int, int)}
     */
    private void drawSelectionBox(int x1, int y1, int x2, int y2) {
        ILocation loc = getLocation();
        x1 += loc.getXPos();
        x2 += loc.getXPos();
        y1 += loc.getYPos();
        y2 += loc.getYPos();

        this.textField.getTextField().drawSelectionBox(x1, y1, x2, y2);
//        if (x1 < x2) {
//            int i = x1;
//            x1 = x2;
//            x2 = i;
//        }
//
//        if (y1 < y2) {
//            int j = y1;
//            y1 = y2;
//            y2 = j;
//        }
//
//        x2 = Math.min(x2, this.getLocation().getXWidth());
//        x1 = Math.min(x1, this.getLocation().getXWidth());
//
//        Tessellator tessellator = Tessellator.getInstance();
//        BufferBuilder bufferbuilder = tessellator.getBuffer();
//        GlStateManager.color4f(0.0F, 0.0F, 255.0F, 255.0F);
//        GlStateManager.disableTexture();
//        GlStateManager.enableColorLogicOp();
//        GlStateManager.logicOp(GlStateManager.LogicOp.OR_REVERSE);
//        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
//        bufferbuilder.pos(x1, y2, 0.0D).endVertex();
//        bufferbuilder.pos(x2, y2, 0.0D).endVertex();
//        bufferbuilder.pos(x2, y1, 0.0D).endVertex();
//        bufferbuilder.pos(x1, y1, 0.0D).endVertex();
//        tessellator.draw();
//        GlStateManager.disableColorLogicOp();
//        GlStateManager.enableTexture();
    }

    @Override
    public void tick() {
        this.cursorCounter++;
    }

    public List<String> getWrappedLines() {
        return fr.listFormattedStringToWidth(textField.getValue(), getLocation().getWidth());
    }

    private List<ITextComponent> getFormattedLines() {
        spellcheck.checkSpelling(getText());
        BiFunction<String, Integer, ITextComponent> formatter = textFormatter.andThen(new SpellingFormatter(spellcheck));
        List<ITextComponent> lines = new ArrayList<>();
        int length = 0;
        for (String line : getWrappedLines()) {
            lines.add(formatter.apply(line, length));
            length += line.length();
        }
        return lines;
    }

    public void setTextFormatter(BiFunction<String, Integer, String> textFormatter) {
        this.textFormatter = textFormatter;
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
    public boolean charTyped(char key, int mods) {
        try {
            return IGuiEventListenerDelegate.super.charTyped(key, mods);
        } finally {
            spellcheck.checkSpelling(getText());
        }
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
                if (getText().charAt(index) == ' ') {
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
