package mnm.mods.tabbychat.settings.gui;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.GeneralSettings;

public class GuiSettingsGeneral extends GuiSettings<GeneralSettings> {

    @Override
    protected GeneralSettings getSettings() {
        // TODO Auto-generated method stub
        return TabbyChat.getInstance().generalSettings;
    }

}
