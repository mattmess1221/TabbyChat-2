package mnm.mods.tabbychat.settings;

import java.net.InetSocketAddress;

import mnm.mods.tabbychat.filters.ChatFilter.FilterList;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.SettingValue;

public class ServerSettings extends AbstractServerSettings {

    public SettingValue<Boolean> channelsEnabled = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> pmEnabled = new SettingValue<Boolean>(true);
    public SettingValue<ChannelPatterns> channelPattern = new SettingValue<ChannelPatterns>(
            ChannelPatterns.BRACKETS);
    public SettingValue<MessagePatterns> messegePattern = new SettingValue<MessagePatterns>(
            MessagePatterns.VANILLA);
    public SettingValue<String> ignoredChannels = new SettingValue<String>("");
    public SettingValue<String> defaultChannels = new SettingValue<String>("");
    public SettingValue<FilterList> filters = new SettingValue<FilterList>(new FilterList());

    public ServerSettings(InetSocketAddress url) {
        super(url, "server");

        registerSetting("channelsEnabled", channelsEnabled);
        registerSetting("pmEnabled", pmEnabled);
        registerSetting("channelPattern", channelPattern);
        registerSetting("messagePattern", messegePattern);
        registerSetting("ignoredChannels", ignoredChannels);
        registerSetting("defaultChannels", defaultChannels);
        registerSetting("filters", filters);
    }
}
