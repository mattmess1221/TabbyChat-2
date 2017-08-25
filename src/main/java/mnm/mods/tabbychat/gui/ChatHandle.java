package mnm.mods.tabbychat.gui;

import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.gui.GuiComponent;

import java.awt.Dimension;
import javax.annotation.Nonnull;

public class ChatHandle extends GuiComponent {

    ChatHandle() {
        setLocation(new Location(0, 0, 10, 10));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        ILocation loc = getLocation();
        this.drawHorizontalLine(3, loc.getWidth() - 4, 3, getPrimaryColorProperty().getHex());
        this.drawVerticalLine(loc.getWidth() - 4, 3, loc.getHeight() - 3, getPrimaryColorProperty().getHex());
    }

    @Nonnull
    @Override
    public Color getPrimaryColorProperty() {
        int opac = (int)(mc.gameSettings.chatOpacity * 255);
        return isHovered() ? Color.of(255, 255, 160, opac) : Color.of(255, 255, 255, opac);
    }

    @Nonnull
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(12, 12);
    }
}
