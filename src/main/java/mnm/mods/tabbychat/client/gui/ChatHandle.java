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
        int color = getColor(loc.contains(mouseX, mouseY)).getHex();
        this.drawHorizontalLine(loc.getXPos() + 3, loc.getXWidth() - 4, loc.getYPos() + 4, color);
        this.drawVerticalLine(loc.getXWidth() - 4, loc.getYPos() + 4, loc.getYHeight() - 2, color);
    }

    @Nonnull
    private Color getColor(boolean hovered) {
        int opac = (int) (mc.gameSettings.chatOpacity * 255);
        return Color.of(255, 255, hovered ? 160 : 255, opac);
    }

    @Nonnull
    @Override
    public Dim getMinimumSize() {
        return new Dim(12, 12);
    }
}
