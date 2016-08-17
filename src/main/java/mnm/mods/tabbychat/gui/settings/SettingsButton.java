package mnm.mods.tabbychat.gui.settings;

import java.awt.Dimension;

import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.config.SettingPanel;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class SettingsButton extends GuiButton {

    private SettingPanel<?> settings;
    private int displayX = 30;
    private boolean active;

    public SettingsButton(SettingPanel<?> settings) {
        super(settings.getDisplayString());
        this.settings = settings;
        this.setLocation(new Location(0, 0, 75, 20));
        this.setSecondaryColor(settings.getSecondaryColor());
    }

    public SettingPanel<?> getSettings() {
        return settings;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (active && displayX > 20) {
            displayX -= 2;
        } else if (!active && displayX < 30) {
            displayX += 2;
        }
        ILocation loc = this.getLocation();
        GlStateManager.enableAlpha();
        Gui.drawRect(displayX - 30, 2, loc.getWidth() + displayX - 30, loc.getHeight() - 2,
                getSecondaryColor().getHex());
        String string = mc.fontRendererObj.trimStringToWidth(getText(), loc.getWidth());
        mc.fontRendererObj.drawString(string, displayX - 20, 6, getPrimaryColorProperty().getHex());
    }

    @Override
    public Dimension getMinimumSize() {
        return getLocation().getSize();
    }
}
