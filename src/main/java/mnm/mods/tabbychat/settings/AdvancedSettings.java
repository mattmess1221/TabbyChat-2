package mnm.mods.tabbychat.settings;

import mnm.mods.util.SettingValue;

public class AdvancedSettings extends TabbySettings {

    public SettingValue<Integer> chatX = new SettingValue<Integer>(5);
    public SettingValue<Integer> chatY = new SettingValue<Integer>(17);
    public SettingValue<Integer> chatW = new SettingValue<Integer>(300);
    public SettingValue<Integer> chatH = new SettingValue<Integer>(160);
    public SettingValue<Float> unfocHeight = new SettingValue<Float>(0.5F);
    public SettingValue<Integer> fadeTime = new SettingValue<Integer>(200);
    public SettingValue<Integer> historyLen = new SettingValue<Integer>(100);
    public SettingValue<Integer> msgDelay = new SettingValue<Integer>(500);

    public AdvancedSettings() {
        super("advanced");

        registerSetting("chatX", chatX);
        registerSetting("chatY", chatY);
        registerSetting("chatW", chatW);
        registerSetting("chatH", chatH);
        registerSetting("unfocHeight", unfocHeight);
        registerSetting("fadeTime", fadeTime);
        registerSetting("historyLen", historyLen);
        registerSetting("msgDelay", msgDelay);
    }
}
