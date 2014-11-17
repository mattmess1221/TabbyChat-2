package mnm.mods.tabbychat.settings;

import mnm.mods.util.Color;
import mnm.mods.util.SettingValue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class ColorSettings extends TabbySettings {

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Color.class,
            new Color.ColorAdapter()).create();

    private static final String CHAT_BOX_COLOR = "chatBoxColor";
    private static final String CHAT_TEXT_COLOR = "chatTextColor";

    public SettingValue<Color> chatBoxColor = new SettingValue<Color>(new Color(0, 0, 0, 127));
    public SettingValue<Color> chatTxtColor = new SettingValue<Color>(new Color(255, 255, 255, 255));

    public ColorSettings() {
        super("colors");
    }

    @Override
    protected void saveSettings() {
        saveSetting(CHAT_BOX_COLOR, gson.toJsonTree(chatBoxColor.getValue()));
        saveSetting(CHAT_TEXT_COLOR, gson.toJsonTree(chatTxtColor.getValue()));
    }

    @Override
    protected void loadSetting(String setting, JsonElement value) {
        if (setting.equals(CHAT_BOX_COLOR)) {
            chatBoxColor.setValue(gson.fromJson(value, Color.class));
        } else if (setting.equals(CHAT_TEXT_COLOR)) {
            chatTxtColor.setValue(gson.fromJson(value, Color.class));
        }
    }
}
