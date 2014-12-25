package mnm.mods.tabbychat.gui.settings;

import java.awt.Dimension;

import mnm.mods.util.gui.GuiButton;
import mnm.mods.util.gui.SettingPanel;
import net.minecraft.client.gui.Gui;

public class SettingsButton extends GuiButton {

    private SettingPanel settings;
    private int displayX = 30;
    private boolean active;

    public SettingsButton(SettingPanel settings) {
        super(settings.getDisplayString());
        this.settings = settings;
        this.setSize(75, 20);
        this.setBackColor(settings.getBackColor());
    }

    public SettingPanel getSettings() {
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
        Gui.drawRect(displayX - 30, 2, getBounds().width + displayX - 30, getBounds().height - 2,
                getBackColor());
        String string = mc.fontRendererObj.trimStringToWidth(getText(), getBounds().width);
        mc.fontRendererObj.drawString(string, displayX - 20, 6, getForeColor());
    }

    @Override
    public Dimension getPreferedSize() {
        return getBounds().getSize();
    }
}
