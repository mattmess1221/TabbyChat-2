package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.util.gui.GuiComponent;

public class ChatHandle extends GuiComponent {

    public ChatHandle() {
        setSize(10, 10);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        this.drawHorizontalLine(3, getBounds().width - 4, 3, getForeColor());
        this.drawVerticalLine(getBounds().width - 4, 3, getBounds().height - 3, getForeColor());
    }

    @Override
    public int getForeColor() {
        return isHovered() ? 0xaaffffa0 : super.getForeColor();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(12, 12);
    }
}
