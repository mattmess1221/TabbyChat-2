package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.util.Color;
import mnm.mods.util.gui.SettingPanel;

public class GuiSettingsColors extends SettingPanel {

    public GuiSettingsColors() {
        setBackColor(Color.getColor(0, 255, 0, 64));
    }

    @Override
    public ColorSettings getSettings() {
        return TabbyChat.getInstance().colorSettings;
    }

    @Override
    public String getDisplayString() {
        return "Colors";
    }

}
