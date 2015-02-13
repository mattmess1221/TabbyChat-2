package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.tabbychat.util.Translation;
import mnm.mods.util.SettingValue;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonElement;

public class GeneralSettings extends TabbySettings {

    private static final String LOG_CHAT = "logChat";
    private static final String SPLIT_LOG = "splitLog";
    private static final String TIMESTAMP_CHAT = "timestampChat";
    private static final String TIMESTAMP_STYLE = "timestampStyle";
    private static final String TIMESTAMP_COLOR = "timestampColor";
    private static final String ANTI_SPAM = "antiSpam";
    private static final String UNREAD_FLASHING = "unreadFlashing";
    private static final String CHECK_UPDATES = "checkUpdates";

    public SettingValue<Boolean> logChat = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> splitLog = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> timestampChat = new SettingValue<Boolean>(false);
    public SettingValue<TimeStamps> timestampStyle = new SettingValue<TimeStamps>(
            TimeStamps.MILITARYSECONDS);
    public SettingValue<EnumChatFormatting> timestampColor = new SettingValue<EnumChatFormatting>(
            EnumChatFormatting.WHITE);
    public SettingValue<Boolean> antiSpam = new SettingValue<Boolean>(false);
    public SettingValue<Boolean> unreadFlashing = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> checkUpdates = new SettingValue<Boolean>(true);

    public GeneralSettings() {
        super(Translation.SETTINGS_GENERAL.translate());
    }

    @Override
    protected void loadSetting(String setting, JsonElement value) {
        if (setting.equals(LOG_CHAT)) {
            logChat.setValue(value.getAsBoolean());
        } else if (setting.equals(SPLIT_LOG)) {
            splitLog.setValue(value.getAsBoolean());
        } else if (setting.equals(TIMESTAMP_CHAT)) {
            timestampChat.setValue(value.getAsBoolean());
        } else if (setting.equals(TIMESTAMP_STYLE)) {
            timestampStyle.setValue(TimeStamps.valueOf(value.getAsString()));
        } else if (setting.equals(TIMESTAMP_COLOR)) {
            timestampColor.setValue(EnumChatFormatting.getValueByName(value.getAsString()));
        } else if (setting.equals(ANTI_SPAM)) {
            antiSpam.setValue(value.getAsBoolean());
        } else if (setting.equals(UNREAD_FLASHING)) {
            unreadFlashing.setValue(value.getAsBoolean());
        } else if (setting.equals(CHECK_UPDATES)) {
            checkUpdates.setValue(value.getAsBoolean());
        }
    }

    @Override
    protected void saveSettings() {
        saveSetting(LOG_CHAT, logChat.getValue());
        saveSetting(SPLIT_LOG, splitLog.getValue());
        saveSetting(TIMESTAMP_CHAT, timestampChat.getValue());
        saveSetting(TIMESTAMP_STYLE, timestampStyle.getValue().name());
        saveSetting(TIMESTAMP_COLOR, timestampColor.getValue().getFriendlyName());
        saveSetting(ANTI_SPAM, antiSpam.getValue());
        saveSetting(UNREAD_FLASHING, unreadFlashing.getValue());
        saveSetting(CHECK_UPDATES, checkUpdates.getValue());
    }
}
