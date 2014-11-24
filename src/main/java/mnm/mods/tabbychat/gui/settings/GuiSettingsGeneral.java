package mnm.mods.tabbychat.gui.settings;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.settings.GeneralSettings;
import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.Color;
import mnm.mods.util.SettingValue;
import mnm.mods.util.gui.GuiSettingBoolean;
import mnm.mods.util.gui.GuiSettingEnum;
import mnm.mods.util.gui.GuiSettingString;
import mnm.mods.util.gui.SettingPanel;

public class GuiSettingsGeneral extends SettingPanel {

    @Override
    public void initGUI() {
        // TODO Grid layout
        addComponent(new GuiSettingBoolean(getSettings().antiSpam, 20, 10, "Anti spam"));
        addComponent(new GuiSettingBoolean(getSettings().timestampChat, 20, 30, "Timestamp chat"));
        addComponent(new GuiSettingEnum<TimeStamps>(getSettings().timestampStyle, 40, 50, 70, 15));
        addComponent(new GuiSettingString(new SettingValue<String>("derp"), 40, 70, 100, 15));
    }

    @Override
    public GeneralSettings getSettings() {
        return TabbyChat.getInstance().generalSettings;
    }

    @Override
    public String getDisplayString() {
        return "General";
    }

    @Override
    public int getBackColor() {
        return Color.getColor(255, 0, 255, 64);
    }

}
