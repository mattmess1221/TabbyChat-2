package mnm.mods.tabbychat.gui;

import com.google.common.eventbus.Subscribe;
import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.gui.ChatInput;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.extra.spell.Spellcheck;
import mnm.mods.tabbychat.extra.spell.SpellingFormatter;
import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.text.FancyFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class TextBox extends GuiComponent implements ChatInput {

    private static final TexturedModal MODAL = new TexturedModal(ChatBox.GUI_LOCATION, 0, 219, 254, 37);

    private FontRenderer fr = mc.fontRendererObj;
    // Dummy textField
    private GuiText textField = new GuiText(new GuiTextField(0, mc.fontRendererObj, 0, 0, 0, 0) {
        @Override
        public void drawTextBox() {
            // noop
        }
    });
    private int cursorCounter;
    private Spellcheck spellcheck;

    TextBox() {
        textField.getTextField().setMaxStringLength(ChatManager.MAX_CHAT_LENGTH);
        textField.setFocused(true);
        textField.getTextField().setCanLoseFocus(false);

        spellcheck = TabbyChat.getInstance().getSpellcheck();
    }

    @Override
    public void onClosed() {
        this.textField.setValue("");
        super.onClosed();
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        GlStateManager.enableBlend();
        drawModalCorners(MODAL, getLocation());
        GlStateManager.disableBlend();

        drawText();
        drawCursor();

    }

    private void drawModalCorners(TexturedModal modal, ILocation location) {
        int x = 0;
        int y = -1;

        int w = location.getWidth() / 2;
        int w2 = location.getWidth() - w;
        int h = location.getHeight() / 2;
        int h2 = location.getHeight() - h;

        int mx = modal.getXPos();
        int my = modal.getYPos();
        int mw = modal.getWidth() - w2;
        int mh = modal.getHeight() - h2;

        // bind the texture
        mc.getTextureManager().bindTexture(modal.getResourceLocation());

        // top left
        drawTexturedModalRect(x, y, mx, my, w, h);
        // top right
        drawTexturedModalRect(x + w, y, mx + mw, my, w2, h);
        // bottom left
        drawTexturedModalRect(x, y + h, mx, my + mh, w, h2);
        // bottom right
        drawTexturedModalRect(x + w, y + h, mx + mw, my + mh, w2, h2);
    }

    private void drawCursor() {
        boolean cursorBlink = this.cursorCounter / 6 % 3 != 0;
        if (cursorBlink) {
            final int HEIGHT = fr.FONT_HEIGHT + 2;
            int xPos = 0;
            int yPos = -4;
            int counter = -1;
            List<String> list = getWrappedLines();
            GuiTextField textField = this.textField.getTextField();
            int size = textField.getSelectedText().length();
            if (textField.getSelectionEnd() < textField.getCursorPosition()) {
                size *= -1;
            }

            // Count up to the target position.
            countLoop:
            for (String text : list) {
                xPos = 0;
                yPos += HEIGHT;
                for (char c : text.toCharArray()) {
                    counter++;
                    // should only happen with spaces
                    while (c != ' ' && textField.getText().charAt(counter) == ' ') {
                        counter++;// skip
                    }
                    if (counter >= textField.getCursorPosition() + size) {
                        break countLoop;
                    }
                    xPos += fr.getCharWidth(c);
                }
            }

            if (textField.getCursorPosition() + size < this.textField.getValue().length()) {
                drawVerticalLine(xPos, yPos - fr.FONT_HEIGHT, yPos, 0xffd0d0d0);
            } else {
                drawHorizontalLine(xPos + 1, xPos + 6, yPos, 0xffd0d0d0);
            }
        }

    }

    private void drawText() {
        FancyFontRenderer ffr = new FancyFontRenderer(mc.fontRendererObj);
        // selection
        boolean started = false;
        boolean ended = false;
        GuiTextField textField = this.textField.getTextField();

        int yPos = 0;
        int pos = 0;
        List<ITextComponent> lines = getFormattedLines();
        for (ITextComponent line : lines) {
            Color color = Color.WHITE;
            ffr.drawChat(line, 2, yPos, color.getHex(), false);
            int xPos = 1;
            for (Character c : line.getUnformattedText().toCharArray()) {
                int width = fr.getCharWidth(c);
                int cursorPos = textField.getCursorPosition();
                int selectDist = textField.getSelectedText().length();
                if (textField.getSelectionEnd() < textField.getCursorPosition()) {
                    selectDist *= -1;
                }
                if (textField.getSelectedText().length() > 0) {
                    if (!started && pos == Math.min(cursorPos, cursorPos + selectDist)) {
                        // Mark for highlighting
                        started = true;
                    }

                    if (started && !ended) {
                        Gui.drawRect(xPos, yPos - 1, xPos + width, yPos + fr.FONT_HEIGHT + 1, getSecondaryColorProperty().getHex());
                    }

                    if (!ended && pos == Math.max(cursorPos, selectDist + cursorPos) - 1) {
                        // unmark for highlighting
                        ended = true;
                    }
                }
                xPos += width;
                pos++;
            }
            yPos += fr.FONT_HEIGHT + 2;
        }

    }

    @Override
    public void updateComponent() {
        this.cursorCounter++;
    }

    @Override
    public List<String> getWrappedLines() {
        return fr.listFormattedStringToWidth(textField.getValue(), getBounds().width);
    }

    private List<ITextComponent> getFormattedLines() {
        List<String> lines = getWrappedLines();
        if (TabbyChat.getInstance().settings.advanced.spelling.get()) {
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
    public Dimension getMinimumSize() {
        return new Dimension(100, (fr.FONT_HEIGHT + 2) * getWrappedLines().size());
    }

    public GuiText getTextField() {
        return textField;
    }

    @Override
    public String getText() {
        return textField.getValue();
    }

    @Override
    public void setText(String text) {
        textField.setValue(text);
    }

    @Subscribe
    public void onMouseClick(GuiMouseEvent event) {
        mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton());
    }

    private void mouseClicked(int x, int y, int mouseButton) {
        if (mouseButton == 0) {
            Rectangle bounds = this.getBounds();

            int width = bounds.width;
            int row = y / (fr.FONT_HEIGHT + 2);

            List<String> lines = getWrappedLines();
            if (row < 0 || row >= lines.size() || x < 0 || x > width) {
                return;
            }
            int index = 0;
            for (int i = 0; i < row; i++) {
                index += lines.get(i).length();
            }
            index += fr.trimStringToWidth(lines.get(row), x - 2).length();
            textField.getTextField().setCursorPosition(index + 1);
        }
    }

    @Override
    public Rectangle getBounds() {
        return this.getLocation().asRectangle();
    }

    @Override
    public boolean isVisible() {
        return super.isVisible() && GuiNewChatTC.getInstance().getChatOpen();
    }
}
