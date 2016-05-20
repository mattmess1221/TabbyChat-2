package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.eventbus.Subscribe;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.gui.ChatInput;
import mnm.mods.tabbychat.core.GuiChatTC;
import mnm.mods.tabbychat.extra.spell.Spellcheck;
import mnm.mods.tabbychat.extra.spell.SpellingFormatter;
import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.text.FancyFontRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class TextBox extends ChatGui implements ChatInput {

    private FontRenderer fr = mc.fontRendererObj;
    // Dummy textField
    private GuiText textField = new GuiText();
    private int cursorCounter;
    private Spellcheck spellcheck;

    public TextBox() {
        super();
        textField.getTextField().setMaxStringLength(300);
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
        Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor().getHex());
        drawText();
        drawCursor();
        drawBorders(0, 0, getBounds().width, getBounds().height, getForeColor().getHex());
        super.drawComponent(mouseX, mouseY);

    }

    private void drawCursor() {
        boolean cursorBlink = this.cursorCounter / 6 % 3 != 0;
        if (cursorBlink) {
            final int HEIGHT = fr.FONT_HEIGHT + 2;
            int xPos = 0;
            int yPos = -2;
            int counter = -1;
            List<String> list = getWrappedLines();
            GuiTextField textField = this.textField.getTextField();
            int size = textField.getSelectedText().length();
            if (textField.getSelectionEnd() < textField.getCursorPosition()) {
                size *= -1;
            }

            // Count up to the target position.
            countLoop: for (String text : list) {
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

        int yPos = 2;
        int pos = 0;
        List<ITextComponent> lines = getFormattedLines();
        for (ITextComponent line : lines) {
            Color color = TabbyChat.getInstance().settings.colors.chatTextColor.get();
            ffr.drawChat(line, 1, yPos, color.getHex(), false);
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
                        Gui.drawRect(xPos, yPos - 1, xPos + width, yPos + fr.FONT_HEIGHT + 1,
                                getBackColor().getHex());
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
        // write the num of sends
        Channel active = TabbyAPI.getAPI().getChat().getActiveChannel();
        String chat = textField.getText().trim().replaceAll("  +", " ");
        String[] msg = GuiChatTC.processSends(chat, active.getPrefix(), active.isPrefixHidden());

        if (msg != null && msg.length > 0) {
            int size = msg.length;
            int color = 0x666666;
            if (!chat.endsWith(msg[size - 1])) {
                // WARNING! Message will get cut off!
                color = 0xff6666;
            }
            int sizeW = fr.getStringWidth(size + "");
            fr.drawString(size + "", getBounds().width - sizeW, 2, color);
        }
    }

    @Override
    public void updateComponent() {
        super.updateComponent();
        this.cursorCounter++;

        List<String> list = getWrappedLines();
        int newHeight = Math.max(1, list.size()) * (fr.FONT_HEIGHT + 2);
        // int newY = getBounds().y + getBounds().height - newHeight;
        this.setSize(getMinimumSize().width, newHeight);

        Color color = getParent().getBackColor();
        Color bkg = Color.of(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() / 4 * 3);

        this.setBackColor(bkg);
    }

    @Override
    public List<String> getWrappedLines() {
        return fr.listFormattedStringToWidth(textField.getValue(), getBounds().width);
    }

    public List<ITextComponent> getFormattedLines() {
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
            int col = x;

            List<String> lines = getWrappedLines();
            if (row < 0 || row >= lines.size() || col < 0 || col > width) {
                return;
            }
            int index = 0;
            for (int i = 0; i < row; i++) {
                index += lines.get(i).length();
            }
            index += fr.trimStringToWidth(lines.get(row), col).length();
            textField.getTextField().setCursorPosition(index + 1);
        }
    }
}
