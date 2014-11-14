package mnm.mods.tabbychat.settings.gui;

import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.util.gui.BorderLayout;
import mnm.mods.util.gui.ComponentScreen;

public abstract class GuiSettings extends ComponentScreen {

    @Override
    public void initGui() {
        getPanel().setLayout(new BorderLayout());
        getPanel().setBounds(mc.displayWidth / 2 - 200, mc.displayHeight / 2 - 150, 400, 300);
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void onGuiClosed() {
        this.getSettings().saveSettings();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    protected abstract TabbySettings getSettings();
}
