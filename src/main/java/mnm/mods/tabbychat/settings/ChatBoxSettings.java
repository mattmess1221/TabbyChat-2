package mnm.mods.tabbychat.settings;

import mnm.mods.util.SettingValue;

import com.google.gson.JsonElement;

public class ChatBoxSettings extends TabbySettings {

    private static final String X_POS = "x";
    private static final String Y_POS = "y";
    private static final String WIDTH = "w";
    private static final String HEIGHT = "h";
    private static final String FADE_TIME = "fadeTime";

    public SettingValue<Integer> xPos = new SettingValue<Integer>(5);
    public SettingValue<Integer> yPos = new SettingValue<Integer>(17);
    public SettingValue<Integer> width = new SettingValue<Integer>(300);
    public SettingValue<Integer> height = new SettingValue<Integer>(160);
    public SettingValue<Integer> fadeTime = new SettingValue<Integer>(200);

    public ChatBoxSettings() {
        super("chatbox");
    }

    @Override
    protected void saveSettings() {
        saveSetting(X_POS, xPos.getValue());
        saveSetting(Y_POS, yPos.getValue());
        saveSetting(WIDTH, width.getValue());
        saveSetting(HEIGHT, height.getValue());
        saveSetting(FADE_TIME, fadeTime.getValue());
    }

    @Override
    protected void loadSetting(String setting, JsonElement value) {
        if (setting.equals(X_POS)) {
            xPos.setValue(value.getAsInt());
        } else if (setting.equals(Y_POS)) {
            yPos.setValue(value.getAsInt());
        } else if (setting.equals(WIDTH)) {
            width.setValue(value.getAsInt());
        } else if (setting.equals(HEIGHT)) {
            height.setValue(value.getAsInt());
        } else if (setting.equals(FADE_TIME)) {
            fadeTime.setValue(value.getAsInt());
        }
    }
}
