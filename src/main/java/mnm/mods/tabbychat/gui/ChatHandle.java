package mnm.mods.tabbychat.gui;

import mnm.mods.util.Color;
import mnm.mods.util.Dim;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.gui.GuiComponent;

import javax.annotation.Nonnull;

public class ChatHandle extends GuiComponent {

    ChatHandle() {
        setLocation(new Location(0, 0, 10, 10));
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        ILocation loc = getLocation();
        this.drawHorizontalLine(loc.getXPos() + 3, loc.getXWidth() - 4, loc.getYPos() + 4, getPrimaryColorProperty().getHex());
        this.drawVerticalLine(loc.getXWidth() - 4, loc.getYPos() + 4, loc.getYHeight() - 2, getPrimaryColorProperty().getHex());
    }

    @Nonnull
    @Override
    public Color getPrimaryColorProperty() {
        int opac = (int)(mc.gameSettings.chatOpacity * 255);
        return isHovered() ? Color.of(255, 255, 160, opac) : Color.of(255, 255, 255, opac);
    }

    @Nonnull
    @Override
    public Dim getMinimumSize() {
        return new Dim(12, 12);
    }
}
