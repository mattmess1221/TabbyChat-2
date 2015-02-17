package mnm.mods.tabbychat.settings;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import mnm.mods.tabbychat.filters.ChatFilter;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.SettingValue;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class ServerSettings extends AbstractServerSettings {

    private static final String CHANNELS_ENABLED = "channelsEnabled";
    private static final String PM_ENABLED = "pmEnabled";
    private static final String CHANNEL_PATTERN = "channelPattern";
    private static final String MESSAGE_PATTERN = "messagePattern";
    private static final String IGNORED_CHANNELS = "ignoredChannels";
    private static final String DEFAULT_CHANNELS = "defaultChannels";
    private static final String FILTERS = "filters";

    public SettingValue<Boolean> channelsEnabled = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> pmEnabled = new SettingValue<Boolean>(true);
    public SettingValue<ChannelPatterns> channelPattern = new SettingValue<ChannelPatterns>(
            ChannelPatterns.BRACKETS);
    public SettingValue<MessagePatterns> messegePattern = new SettingValue<MessagePatterns>(
            MessagePatterns.VANILLA);
    public SettingValue<String> ignoredChannels = new SettingValue<String>("");
    public SettingValue<String> defaultChannels = new SettingValue<String>("");
    public SettingValue<List<ChatFilter>> filters = new SettingValue<List<ChatFilter>>(
            new ArrayList<ChatFilter>());

    public ServerSettings(InetSocketAddress url) {
        super(url, "server");
    }

    @Override
    protected void saveSettings() {
        this.saveSetting(CHANNELS_ENABLED, channelsEnabled.getValue());
        this.saveSetting(PM_ENABLED, pmEnabled.getValue());
        this.saveSetting(CHANNEL_PATTERN, channelPattern.getValue().name());
        this.saveSetting(MESSAGE_PATTERN, messegePattern.getValue().name());
        this.saveSetting(IGNORED_CHANNELS, gson.toJsonTree(ignoredChannels.getValue()));
        this.saveSetting(DEFAULT_CHANNELS, gson.toJsonTree(defaultChannels.getValue()));
        this.saveSetting(FILTERS, gson.toJsonTree(filters.getValue()));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadSetting(String setting, JsonElement value) {
        final Type typeListFilter = new TypeToken<List<ChatFilter>>() {}.getType();

        // JsonObject obj = value.getAsJsonObject();
        if (setting.equals(CHANNELS_ENABLED)) {
            channelsEnabled.setValue(value.getAsBoolean());
        } else if (setting.equals(PM_ENABLED)) {
            pmEnabled.setValue(value.getAsBoolean());
        } else if (setting.equals(CHANNEL_PATTERN)) {
            channelPattern.setValue(ChannelPatterns.valueOf(value.getAsString()));
        } else if (setting.equals(MESSAGE_PATTERN)) {
            messegePattern.setValue(MessagePatterns.valueOf(value.getAsString()));
        } else if (setting.equals(IGNORED_CHANNELS) && value.isJsonPrimitive()) {
            ignoredChannels.setValue(value.getAsString());
        } else if (setting.equals(DEFAULT_CHANNELS) && value.isJsonPrimitive()) {
            defaultChannels.setValue(value.getAsString());
        } else if (setting.equals(FILTERS)) {
            filters.setValue((List<ChatFilter>) gson.fromJson(value, typeListFilter));
        }
    }
}
