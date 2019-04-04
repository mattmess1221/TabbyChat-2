package mnm.mods.tabbychat.client.gui;

import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;

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