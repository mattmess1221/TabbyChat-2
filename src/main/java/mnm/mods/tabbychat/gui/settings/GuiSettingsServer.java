package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.Color;
import mnm.mods.util.Settings;
import mnm.mods.util.gui.SettingPanel;

public class GuiSettingsServer extends SettingPanel {

    public GuiSettingsServer() {
        setBackColor(Color.getColor(0, 50, 200, 64));
    }

    @Override
    public String getDisplayString() {
        // TODO Translate
        return "Filters";
    }

    @Override
    public Settings getSettings() {
        return TabbyChat.getInstance().channelSettings;
    }

}
