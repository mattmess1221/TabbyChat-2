package mnm.mods.tabbychat.gui;

import java.awt.Dimension;
import java.util.List;

import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.events.GuiMouseAdapter;
import mnm.mods.util.gui.events.GuiMouseEvent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import org.lwjgl.input.Keyboard;

public class TextBox extends GuiComponent {

    private FontRenderer fr = mc.fontRendererObj;
    private String text = "";
    private int cursorPos = 0;
    private int selectDist = 0;
    private int cursorCounter;

    public TextBox() {
        super();
        this.addEventListener(new GuiMouseAdapter() {

            @Override
            public void mousePressed(GuiMouseEvent event) {
                // TODO Move cursor
            }

            @Override
            public void mouseDragged(GuiMouseEvent event) {
                // TODO Select text
            }
        });
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (GuiNewChatTC.getInstance().getChatOpen()) {
            Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
            drawText();
            drawCursor();
            drawBorders(0, 0, getBounds().width, getBounds().height);
        }
    }

    private void drawCursor() {
        boolean cursorBlink = this.cursorCounter / 6 % 2 == 0;
        if (cursorBlink) {
            char marker;
            int xPos = 0;
            int yPos = 2;
            int counter = -1;
            List<String> list = getWrappedLines();

            // Count up to the target position.
            countLoop: for (String text : list) {
                for (char c : text.concat(" ").toCharArray()) {
                    counter++;
                    if (counter >= this.getCursorPosition() + selectDist) {
                        break countLoop;
                    }
                    xPos += fr.getCharWidth(c);
                }
                xPos = 0;
                yPos += fr.FONT_HEIGHT + 2;
            }

            if (this.cursorPos + selectDist < this.text.length()) {
                marker = '|';
            } else {
                marker = '_';
                xPos += 1;
            }
            fr.drawString(Character.toString(marker), xPos, yPos, 0xeeeeee);
        }
    }

    private void drawText() {
        // selection
        boolean started = false;
        boolean ended = false;

        int yPos = 2;
        int pos = 0;
        for (String line : getWrappedLines()) {
            int xPos = 1;
            for (Character c : line.toCharArray()) {
                fr.drawString(c.toString(), xPos, yPos, getForeColor());
                int width = fr.getCharWidth(c);
                if (selectDist != 0) {
                    if (!started && pos == Math.min(cursorPos, cursorPos + selectDist)) {
                        started = true;
                    }

                    if (started && !ended) {
                        Gui.drawRect(xPos, yPos - 1, xPos + width, yPos + fr.FONT_HEIGHT + 1,
                                getBackColor());
                    }

                    if (!ended && pos == Math.max(cursorPos, this.selectDist + this.cursorPos) - 1) {
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
        super.updateComponent();
        this.cursorCounter++;

        @SuppressWarnings("unchecked")
        List<String> list = fr.listFormattedStringToWidth(text, getBounds().width);
        int newHeight = Math.max(1, list.size()) * (fr.FONT_HEIGHT + 2);
        // int newY = getBounds().y + getBounds().height - newHeight;
        this.setSize(getPreferedSize().width, newHeight);

    }

    @SuppressWarnings("unchecked")
    public List<String> getWrappedLines() {
        return fr.listFormattedStringToWidth(text, getBounds().width);
    }

    @Override
    public Dimension getPreferedSize() {
        return new Dimension(100, (fr.FONT_HEIGHT + 2) * getWrappedLines().size());
    }

    public void writeText(String text) {
        // Filter characters
        text = ChatAllowedCharacters.filterAllowedCharacters(text);

        int intStart = Math.min(cursorPos + selectDist, cursorPos);
        int intEnd = Math.max(cursorPos + selectDist, cursorPos);
        String start = getText().substring(0, intStart);
        String end = getText().substring(intEnd);
        this.text = start + text + end;
        // deselect();
        moveCursor(text.length());
    }

    public void deleteSelection() {
        int intStart = Math.min(cursorPos + selectDist, cursorPos);
        int intEnd = Math.max(cursorPos + selectDist, cursorPos);
        String start = getText().substring(0, intStart);
        String end = getText().substring(intEnd);
        deselect();
        setText(start + end);
        setCursorPosition(intStart);
    }

    public void writeText(char c) {
        writeText(Character.toString(c));
    }

    public void setText(String text) {
        clear();
        this.text = text;
        setCursorEnd();
    }

    public String getText() {
        return this.text;
    }

    public int getCursorPosition() {
        return cursorPos;
    }

    public void setCursorPosition(int pos) {
        pos = Math.max(pos, 0);
        pos = Math.min(pos, getText().length());
        cursorPos = pos;
        selectDist = 0;
    }

    public void setCursorEnd() {
        setCursorPosition(getText().length());
    }

    public void setCursorZero() {
        setCursorPosition(0);
    }

    public void moveCursor(int pos) {
        int newPos;
        if (selectDist == 0) {
            newPos = this.cursorPos + pos;
        } else {
            newPos = this.cursorPos + selectDist;
        }
        setCursorPosition(newPos);

    }

    public void selectText(int dist) {
        int min = -(selectDist + cursorPos);
        int max = text.length() - (selectDist + cursorPos);
        dist = Math.max(dist, min);
        dist = Math.min(dist, max);
        this.selectDist += dist;
    }

    public void deleteFromCursor(int i) {
        if (selectDist == 0)
            selectText(i);
        this.deleteSelection();
        if (i > 0 && !getText().substring(getCursorPosition()).isEmpty()) {
            System.out.println(getText().substring(getCursorPosition()));
            // moveCursor(-i);
        }
    }

    public void deselect() {
        setCursorPosition(cursorPos + selectDist);
    }

    public void clear() {
        this.text = "";
        deselect();
        setCursorEnd();
    }

    public String getSelectedText() {
        int start = Math.min(this.cursorPos, cursorPos + this.selectDist);
        int end = Math.max(this.cursorPos, cursorPos + this.selectDist);
        return this.text.substring(start, end);
    }

    public void keyTyped(char key, int code) {
        if (!Character.isISOControl(key)) {
            writeText(key);
        }
        switch (code) {
        case Keyboard.KEY_X:
            // Cut
            if (GuiScreen.isCtrlKeyDown() && this.selectDist != 0) {
                GuiScreen.setClipboardString(this.getSelectedText());
                writeText("");
            }
            break;
        case Keyboard.KEY_C:
            // Copy
            if (GuiScreen.isCtrlKeyDown() && this.selectDist != 0) {
                GuiScreen.setClipboardString(this.getSelectedText());
            }
            break;
        case Keyboard.KEY_V:
            // Paste
            if (GuiScreen.isCtrlKeyDown()) {
                this.writeText(GuiScreen.getClipboardString());
            }
            break;
        case Keyboard.KEY_A:
            // Select All
            if (GuiScreen.isCtrlKeyDown()) {
                this.setCursorPosition(0);
                this.selectText(text.length());
            }
            break;
        case Keyboard.KEY_LEFT:
            if (GuiScreen.isShiftKeyDown()) {
                this.selectText(-1);
            } else {
                moveCursor(-1);
            }
            break;
        case Keyboard.KEY_RIGHT:
            if (GuiScreen.isShiftKeyDown()) {
                this.selectText(1);
            } else {
                moveCursor(1);
            }
            break;
        case Keyboard.KEY_DELETE:
            // Delete
            this.deleteFromCursor(1);
            break;
        case Keyboard.KEY_BACK:
            // Backspace
            this.deleteFromCursor(-1);
            break;
        case Keyboard.KEY_HOME:
            // Home
            this.setCursorPosition(0);
            break;
        case Keyboard.KEY_END:
            this.setCursorPosition(getText().length());
            break;

        // TODO The rest of the shortcuts
        }
    }

    public String getCurrentWord() {
        String[] words = getText().split(" ");
        String current = "";
        int counter = 0;
        loop: for (String word : words) {
            for (int i = 0; i < word.length(); i++) {
                counter++;
                if (counter == cursorPos) {
                    current = word;
                    break loop;
                }
            }
        }
        return current;
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
        // TODO deobfuscate. Exactly what is this used for?
        int result = p_146197_2_;
        boolean flag1 = p_146197_1_ < 0;
        int l = Math.abs(p_146197_1_);

        for (int i1 = 0; i1 < l; ++i1) {
            if (flag1) {
                while (p_146197_3_ && result > 0 && this.text.charAt(result - 1) == 32) {
                    --result;
                }

                while (result > 0 && this.text.charAt(result - 1) != 32) {
                    --result;
                }
            } else {
                int j1 = this.text.length();
                result = this.text.indexOf(32, result);

                if (result == -1) {
                    result = j1;
                } else {
                    while (p_146197_3_ && result < j1 && this.text.charAt(result) == 32) {
                        ++result;
                    }
                }
            }
        }

        return result;

    }

}
