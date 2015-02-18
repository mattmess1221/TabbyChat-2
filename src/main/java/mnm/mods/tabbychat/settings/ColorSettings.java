package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.Color;
import mnm.mods.util.SettingValue;

import com.google.gson.GsonBuilder;

public class ColorSettings extends TabbySettings {

    public SettingValue<Color> chatBoxColor = new SettingValue<Color>(new Color(0, 0, 0, 127));
    public SettingValue<Color> chatTextColor = new SettingValue<Color>(new Color(255, 255, 255, 255));

    public ColorSettings() {
        super(Translation.SETTINGS_COLORS.translate());

        registerSetting("chatBoxColor", chatBoxColor);
        registerSetting("chatTextColor", chatTextColor);
    }

    @Override
    protected void setupGson(GsonBuilder builder) {
        builder.registerTypeAdapter(Color.class, new Color.ColorAdapter());
    }
}
