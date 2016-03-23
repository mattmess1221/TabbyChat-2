package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueList;
import mnm.mods.util.config.ValueObject;

public class GeneralServerSettings extends ValueObject {

    public Value<Boolean> channelsEnabled = value(true);
    public Value<Boolean> pmEnabled = value(true);
    public Value<ChannelPatterns> channelPattern = value(ChannelPatterns.BRACKETS);
    public Value<MessagePatterns> messegePattern = value(MessagePatterns.WHISPERS);
    public Value<Boolean> useDefaultTab = value(true);
    public ValueList<String> ignoredChannels = list();
    public ValueList<String> defaultChannels = list();
}
