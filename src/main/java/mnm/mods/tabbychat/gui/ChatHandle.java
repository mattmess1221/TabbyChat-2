package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiComponent;

public class ChatHandle extends GuiComponent {

    public ChatHandle() {
        setSize(10, 10);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        this.drawHorizontalLine(3, getBounds().width - 4, 3, getForeColor().getHex());
        this.drawVerticalLine(getBounds().width - 4, 3, getBounds().height - 3, getForeColor().getHex());
    }

    @Override
    public Color getForeColor() {
        return isHovered() ? Color.of(0xaaffffa0) : super.getForeColor();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(12, 12);
    }
}
