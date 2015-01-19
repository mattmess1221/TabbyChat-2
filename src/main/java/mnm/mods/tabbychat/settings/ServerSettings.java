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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class ServerSettings extends AbstractServerSettings {

    private static final String CHANNELS_ENABLED = "channelsEnabled";
    private static final String PM_ENABLED = "pmEnabled";
    private static final String CHANNEL_PATTERN = "channelPattern";
    private static final String MESSAGE_PATTERN = "messagePattern";
    private static final String CUSTOM_CHANNEL = "customChannel";
    private static final String CUSTOM_MESSAGE = "customMessage";
    private static final String IGNORED_CHANNELS = "ignoredChannels";
    private static final String DEFAULT_CHANNELS = "defaultChannels";
    private static final String FILTERS = "filters";

    public SettingValue<Boolean> channelsEnabled = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> pmEnabled = new SettingValue<Boolean>(true);
    public SettingValue<ChannelPatterns> channelPattern = new SettingValue<ChannelPatterns>(
            ChannelPatterns.BRACES);
    public SettingValue<MessagePatterns> messegePattern = new SettingValue<MessagePatterns>(
            MessagePatterns.VANILLA);
    public SettingValue<String> customChannel = new SettingValue<String>("");
    public SettingValue<String> customMessage = new SettingValue<String>("");
    public SettingValue<List<String>> ignoredChannels = new SettingValue<List<String>>(
            new ArrayList<String>());
    public SettingValue<List<String>> defaultChannels = new SettingValue<List<String>>(
            new ArrayList<String>());
    public SettingValue<List<ChatFilter>> filters = new SettingValue<List<ChatFilter>>(
            new ArrayList<ChatFilter>());

    public ServerSettings(InetSocketAddress url) {
        super(url, "server");
    }

    @Override
    protected void saveSettings() {
        this.saveSetting(CHANNEL_PATTERN, channelPattern.getValue().name());
        this.saveSetting(MESSAGE_PATTERN, messegePattern.getValue().name());
        this.saveSetting(CUSTOM_CHANNEL, customChannel.getValue());
        this.saveSetting(CUSTOM_MESSAGE, customMessage.getValue());
        this.saveSetting(IGNORED_CHANNELS, gson.toJsonTree(ignoredChannels.getValue()));
        this.saveSetting(DEFAULT_CHANNELS, gson.toJsonTree(defaultChannels.getValue()));
        this.saveSetting(FILTERS, gson.toJsonTree(filters.getValue()));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadSetting(String setting, JsonElement value) {
        final Type typeListString = new TypeToken<List<String>>() {}.getType();
        final Type typeListFilter = new TypeToken<List<ChatFilter>>() {}.getType();

        JsonObject obj = value.getAsJsonObject();
        if (setting.equals(CHANNELS_ENABLED)) {
            channelsEnabled.setValue(obj.get(CHANNELS_ENABLED).getAsBoolean());
        } else if (setting.equals(PM_ENABLED)) {
            pmEnabled.setValue(obj.get(PM_ENABLED).getAsBoolean());
        } else if (setting.equals(CHANNEL_PATTERN)) {
            String pattern = obj.get(CHANNEL_PATTERN).getAsString();
            channelPattern.setValue(ChannelPatterns.valueOf(pattern));
        } else if (setting.equals(MESSAGE_PATTERN)) {
            String pattern = obj.get(MESSAGE_PATTERN).getAsString();
            messegePattern.setValue(MessagePatterns.valueOf(pattern));
        } else if (setting.equals(CUSTOM_CHANNEL)) {
            customChannel.setValue(obj.get(CUSTOM_CHANNEL).getAsString());
        } else if (setting.equals(CUSTOM_MESSAGE)) {
            customMessage.setValue(obj.get(CUSTOM_MESSAGE).getAsString());
        } else if (setting.equals(IGNORED_CHANNELS)) {
            ignoredChannels.setValue((List<String>) gson.fromJson(obj.get(IGNORED_CHANNELS),
                    typeListString));
        } else if (setting.equals(DEFAULT_CHANNELS)) {
            defaultChannels.setValue((List<String>) gson.fromJson(obj.get(DEFAULT_CHANNELS),
                    typeListString));
        } else if (setting.equals(FILTERS)) {
            filters.setValue((List<ChatFilter>) gson.fromJson(obj.get(FILTERS), typeListFilter));
        }
    }
}
