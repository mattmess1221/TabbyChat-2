package mnm.mods.tabbychat.settings;

import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingObject;
import mnm.mods.util.config.SettingValue;

public class AdvancedSettings extends SettingObject<AdvancedSettings> {

    @Setting
    public SettingValue<Integer> chatX = value(5);
    @Setting
    public SettingValue<Integer> chatY = value(17);
    @Setting
    public SettingValue<Integer> chatW = value(300);
    @Setting
    public SettingValue<Integer> chatH = value(160);
    @Setting
    public SettingValue<Float> unfocHeight = value(0.5F);
    @Setting
    public SettingValue<Integer> fadeTime = value(200);
    @Setting
    public SettingValue<Integer> historyLen = value(100);
    @Setting
    public SettingValue<Integer> msgDelay = value(500);

}
