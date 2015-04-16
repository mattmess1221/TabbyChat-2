package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.FormattingSerializer;
import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.SettingValue;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.GsonBuilder;

public class GeneralSettings extends TabbySettings {

    public SettingValue<Boolean> logChat = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> splitLog = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> timestampChat = new SettingValue<Boolean>(false);
    public SettingValue<TimeStamps> timestampStyle = new SettingValue<TimeStamps>(
            TimeStamps.MILITARYSECONDS);
    public SettingValue<EnumChatFormatting> timestampColor = new SettingValue<EnumChatFormatting>(
            EnumChatFormatting.WHITE);
    public SettingValue<Boolean> antiSpam = new SettingValue<Boolean>(false);
    public SettingValue<Float> antiSpamTolerance = new SettingValue<Float>(0.95f);
    public SettingValue<Boolean> unreadFlashing = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> checkUpdates = new SettingValue<Boolean>(true);

    public GeneralSettings() {
        super("general");

        registerSetting("logChat", logChat);
        registerSetting("splitLog", splitLog);
        registerSetting("timestampChat", timestampChat);
        registerSetting("timestampStyle", timestampStyle);
        registerSetting("timestampColor", timestampColor);
        registerSetting("antiSpam", antiSpam);
        registerSetting("spamTolerance", antiSpamTolerance);
        registerSetting("unreadFlashing", unreadFlashing);
        registerSetting("checkUpdates", checkUpdates);
    }

    @Override
    protected void setupGson(GsonBuilder builder) {
        builder.registerTypeAdapter(EnumChatFormatting.class, new FormattingSerializer());
    }
}
