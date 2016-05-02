package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueObject;
import net.minecraft.util.EnumChatFormatting;

public class GeneralSettings extends ValueObject {

    public Value<Boolean> logChat = value(true);
    public Value<Boolean> splitLog = value(true);
    public Value<Boolean> timestampChat = value(false);
    public Value<TimeStamps> timestampStyle = value(TimeStamps.MILITARYSECONDS);
    public Value<EnumChatFormatting> timestampColor = value(EnumChatFormatting.WHITE);
    public Value<Boolean> antiSpam = value(false);
    public Value<Double> antiSpamPrejudice = value(0D);
    public Value<Boolean> unreadFlashing = value(true);
    public Value<Boolean> checkUpdates = value(true);
}
