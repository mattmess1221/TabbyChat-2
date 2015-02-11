package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.ChannelSettings;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Color;
import mnm.mods.util.gui.SettingPanel;

public class GuiFilterSettings extends SettingPanel<ChannelSettings> {

    public GuiFilterSettings() {
        setBackColor(Color.getColor(0, 50, 200, 64));
        setDisplayString(Translation.SETTINGS_FILTERS.translate());
    }

    @Override
    public void initGUI() {}

    @Override
    public ChannelSettings getSettings() {
        return TabbyChat.getInstance().channelSettings;
    }

}
