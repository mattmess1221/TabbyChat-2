package mnm.mods.tabbychat.settings;

import java.net.InetSocketAddress;

import mnm.mods.tabbychat.extra.filters.ChatFilter.FilterList;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.FormattingSerializer;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.SettingValue;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.GsonBuilder;

public class ServerSettings extends AbstractServerSettings {

    public SettingValue<Boolean> channelsEnabled = new SettingValue<Boolean>(true);
    public SettingValue<Boolean> pmEnabled = new SettingValue<Boolean>(true);
    public SettingValue<ChannelPatterns> channelPattern = new SettingValue<ChannelPatterns>(
            ChannelPatterns.BRACKETS);
    public SettingValue<MessagePatterns> messegePattern = new SettingValue<MessagePatterns>(
            MessagePatterns.VANILLA);
    public SettingValue<Boolean> useDefaultTab = new SettingValue<Boolean>(true);
    public SettingValue<String> ignoredChannels = new SettingValue<String>("");
    public SettingValue<String> defaultChannels = new SettingValue<String>("");
    public SettingValue<FilterList> filters = new SettingValue<FilterList>(new FilterList());

    public ServerSettings(InetSocketAddress url) {
        super(url, "server");

        registerSetting("channelsEnabled", channelsEnabled);
        registerSetting("pmEnabled", pmEnabled);
        registerSetting("channelPattern", channelPattern);
        registerSetting("messagePattern", messegePattern);
        registerSetting("useDefaultTab", useDefaultTab);
        registerSetting("ignoredChannels", ignoredChannels);
        registerSetting("defaultChannels", defaultChannels);
        registerSetting("filters", filters);
    }

    @Override
    protected void setupGson(GsonBuilder builder) {
        builder.registerTypeAdapter(EnumChatFormatting.class, new FormattingSerializer());
    }
}
