package mnm.mods.tabbychat.settings;

import mnm.mods.util.SettingValue;

public class ChatBoxSettings extends TabbySettings {

    public SettingValue<Integer> xPos = new SettingValue<Integer>(5);
    public SettingValue<Integer> yPos = new SettingValue<Integer>(17);
    public SettingValue<Integer> width = new SettingValue<Integer>(300);
    public SettingValue<Integer> height = new SettingValue<Integer>(160);
    public SettingValue<Integer> fadeTime = new SettingValue<Integer>(200);

    public ChatBoxSettings() {
        super("chatbox");

        registerSetting("x", xPos);
        registerSetting("y", yPos);
        registerSetting("w", width);
        registerSetting("h", height);
        registerSetting("fadeTime", fadeTime);
    }
}
