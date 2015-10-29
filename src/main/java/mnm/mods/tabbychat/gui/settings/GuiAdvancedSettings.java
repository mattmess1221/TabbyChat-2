package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.util.gui.config.SettingPanel;

public class GuiAdvancedSettings extends SettingPanel<TabbySettings> {

    @Override
    public TabbySettings getSettings() {
        return TabbyChat.getInstance().settings;
    }
}
