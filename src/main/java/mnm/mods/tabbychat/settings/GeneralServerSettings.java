package mnm.mods.tabbychat.settings;

import com.google.gson.annotations.Expose;

import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.MessagePatterns;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueList;
import mnm.mods.util.config.ValueObject;

public class GeneralServerSettings extends ValueObject {

    @Expose
    public Value<Boolean> channelsEnabled = value(true);
    @Expose
    public Value<Boolean> pmEnabled = value(true);
    @Expose
    public Value<ChannelPatterns> channelPattern = value(ChannelPatterns.BRACKETS);
    @Expose
    public Value<MessagePatterns> messegePattern = value(MessagePatterns.WHISPERS);
    @Expose
    public Value<Boolean> useDefaultTab = value(true);
    @Expose
    public ValueList<String> ignoredChannels = list();
    @Expose
    public ValueList<String> defaultChannels = list();
}
