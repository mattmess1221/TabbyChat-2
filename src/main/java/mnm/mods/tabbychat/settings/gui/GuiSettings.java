package mnm.mods.tabbychat.settings.gui;

import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.util.gui.ComponentScreen;

public abstract class GuiSettings<T extends TabbySettings> extends ComponentScreen {

    @Override
    public void initGui() {
    }

    @Override
    public void updateScreen() {
    }

    @Override
    public void onGuiClosed() {
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    protected abstract T getSettings();
}
