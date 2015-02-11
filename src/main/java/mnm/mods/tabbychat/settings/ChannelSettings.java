package mnm.mods.tabbychat.settings;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.SettingValue;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class ChannelSettings extends AbstractServerSettings {

    private static final String CHANNELS_ENABLED = "channelsEnabled";
    private static final String PM_ENABLED = "pmEnabled";
    private static final String CHANNEL_PATTERNS = "channelPattern";
    private static final String PM_PATTERNS = "pmPattern";
    private static final String CUSTOM_PM_TO_ME = "customPmIncoming";
    private static final String CUSTOM_PM_FROM_ME = "customPmOutgoing";
    private static final String DEFAULT_CHANNELS = "defaultChannels";
    private static final String IGNORED_CHANNELS = "ignoredChannels";
    private static final String USE_IGNORE_PATTERNS = "useIgnorePatterns";

    private static final Type LIST_TYPE = new TypeToken<List<String>>() {}.getType();

    public SettingValue<Boolean> channelsEnabled = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> pmEnabled = new SettingValue<Boolean>(true);
    public SettingValue<ChannelPatterns> channelPattern = new SettingValue<ChannelPatterns>(
            ChannelPatterns.BRACKETS);
    public SettingValue<MessagePatterns> pmPattern = new SettingValue<MessagePatterns>(
            MessagePatterns.VANILLA);
    public SettingValue<String> customPmIncoming = new SettingValue<String>("");
    public SettingValue<String> customPmOutgoing = new SettingValue<String>("");
    public SettingValue<List<String>> defaultChannels = new SettingValue<List<String>>(
            new ArrayList<String>());
    public SettingValue<List<String>> ignoredChannels = new SettingValue<List<String>>(
            new ArrayList<String>());
    public SettingValue<Boolean> useIgnorePatterns = new SettingValue<Boolean>(false);

    public ChannelSettings(InetSocketAddress url) {
        super(url, "server");
    }

    @Override
    protected void saveSettings() {
        this.saveSetting(CHANNELS_ENABLED, channelsEnabled.getValue());
        this.saveSetting(PM_ENABLED, pmEnabled.getValue());
        this.saveSetting(CHANNEL_PATTERNS, channelPattern.getValue().name());
        this.saveSetting(PM_PATTERNS, pmPattern.getValue().name());
        this.saveSetting(CUSTOM_PM_TO_ME, customPmIncoming.getValue());
        this.saveSetting(CUSTOM_PM_FROM_ME, customPmOutgoing.getValue());
        this.saveSetting(DEFAULT_CHANNELS, gson.toJsonTree(defaultChannels.getValue()));
        this.saveSetting(IGNORED_CHANNELS, gson.toJsonTree(ignoredChannels.getValue()));
        this.saveSetting(USE_IGNORE_PATTERNS, useIgnorePatterns.getValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadSetting(String setting, JsonElement value) {

        if (setting.equals(CHANNELS_ENABLED)) {
            channelsEnabled.setValue(value.getAsBoolean());
        } else if (setting.equals(PM_ENABLED)) {
            pmEnabled.setValue(value.getAsBoolean());
        } else if (setting.equals(CHANNEL_PATTERNS)) {
            channelPattern.setValue(ChannelPatterns.valueOf(value.getAsString()));
        } else if (setting.equals(PM_PATTERNS)) {
            pmPattern.setValue(MessagePatterns.valueOf(value.getAsString()));
        } else if (setting.equals(CUSTOM_PM_TO_ME)) {
            customPmIncoming.setValue(value.getAsString());
        } else if (setting.equals(CUSTOM_PM_FROM_ME)) {
            customPmOutgoing.setValue(value.getAsString());
        } else if (setting.equals(DEFAULT_CHANNELS)) {
            defaultChannels
                    .setValue((List<String>) gson.fromJson(value.getAsJsonArray(), LIST_TYPE));
        } else if (setting.equals(IGNORED_CHANNELS)) {
            ignoredChannels
                    .setValue((List<String>) gson.fromJson(value.getAsJsonArray(), LIST_TYPE));
        } else if (setting.equals(USE_IGNORE_PATTERNS)) {
            useIgnorePatterns.setValue(value.getAsBoolean());
        }
    }

}
