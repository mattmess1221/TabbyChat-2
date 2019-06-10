package mnm.mods.tabbychat.client.gui.settings;

import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;
import mnm.mods.tabbychat.client.gui.component.GuiButton;
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel;

import javax.annotation.Nonnull;

public class SettingsButton extends GuiButton {

    private SettingPanel<?> settings;
    private int displayX = 30;
    private boolean active;

    SettingsButton(SettingPanel<?> settings) {
        super(settings.getDisplayString());
        this.settings = settings;
        this.setLocation(new Location(0, 0, 75, 20));
        settings.getSecondaryColor().ifPresent(this::setSecondaryColor);
    }

    public SettingPanel<?> getSettings() {
        return settings;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        if (active && displayX > 20) {
            displayX -= 2;
        } else if (!active && displayX < 30) {
            displayX += 2;
        }
        ILocation loc = this.getLocation();
        int x1 = loc.getXPos() + displayX - 30;
        int x2 = loc.getXWidth() + displayX - 30;
        int y1 = loc.getYPos() + 1;
        int y2 = loc.getYHeight() - 1;

        GlStateManager.enableAlphaTest();
        getSecondaryColor().ifPresent(color -> fill(x1, y1, x2, y2, color.getHex()));
        String string = mc.fontRenderer.trimStringToWidth(getText(), loc.getWidth());
        mc.fontRenderer.drawString(string, x1 + 10, loc.getYCenter() - 4, getPrimaryColorProperty().getHex());
    }

    @Nonnull
    @Override
    public Dim getMinimumSize() {
        return getLocation().getSize();
    }
}
