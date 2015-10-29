package mnm.mods.tabbychat.settings;

import mnm.mods.util.Color;
import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingObject;
import mnm.mods.util.config.SettingValue;

public class ColorSettings extends SettingObject<ColorSettings> {

    @Setting
    public SettingValue<Color> chatBoxColor = value(new Color(0, 0, 0, 127));
    @Setting
    public SettingValue<Color> chatTextColor = value(new Color(255, 255, 255, 255));
}
