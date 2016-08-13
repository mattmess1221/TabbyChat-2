package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.util.Color;
import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.ILocation;
import mnm.mods.util.gui.Location;

public class ChatHandle extends GuiComponent {

    public ChatHandle() {
        setLocation(new Location(0, 0, 10, 10));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        ILocation loc = getLocation();
        this.drawHorizontalLine(3, loc.getWidth() - 4, 3, getForeColor().getHex());
        this.drawVerticalLine(loc.getWidth() - 4, 3, loc.getHeight() - 3, getForeColor().getHex());
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
