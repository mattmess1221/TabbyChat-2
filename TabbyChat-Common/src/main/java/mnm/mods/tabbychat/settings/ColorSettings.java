package mnm.mods.tabbychat.settings;

import mnm.mods.util.Color;
import mnm.mods.util.SettingValue;

public class ColorSettings extends TabbySettings {

    public SettingValue<Color> chatBoxColor = new SettingValue<Color>(new Color(0, 0, 0, 127));
    public SettingValue<Color> chatTextColor = new SettingValue<Color>(new Color(255, 255, 255, 255));

    public ColorSettings() {
        super("colors");

        registerSetting("chatBoxColor", chatBoxColor);
        registerSetting("chatTextColor", chatTextColor);
    }
}
