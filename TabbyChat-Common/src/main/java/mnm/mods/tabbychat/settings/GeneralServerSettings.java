package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingObject;
import mnm.mods.util.config.SettingValue;

public class GeneralServerSettings extends SettingObject<GeneralServerSettings> {

    @Setting
    public SettingValue<Boolean> channelsEnabled = value(true);
    @Setting
    public SettingValue<Boolean> pmEnabled = value(true);
    @Setting
    public SettingValue<ChannelPatterns> channelPattern = value(ChannelPatterns.BRACKETS);
    @Setting
    public SettingValue<MessagePatterns> messegePattern = value(MessagePatterns.VANILLA);
    @Setting
    public SettingValue<Boolean> useDefaultTab = value(true);
    @Setting
    public SettingValue<String> ignoredChannels = value("");
    @Setting
    public SettingValue<String> defaultChannels = value("");
}
