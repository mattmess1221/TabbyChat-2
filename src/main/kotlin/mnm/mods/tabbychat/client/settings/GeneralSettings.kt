package mnm.mods.tabbychat.client.settings;

import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.util.config.ValueObject;
import net.minecraft.util.text.TextFormatting;

public class GeneralSettings extends ValueObject {

    public Value<Boolean> logChat = value(true);
    public Value<Boolean> splitLog = value(true);
    public Value<Boolean> timestampChat = value(false);
    public Value<TimeStamps> timestampStyle = value(TimeStamps.MILITARYSECONDS);
    public Value<TextFormatting> timestampColor = value(TextFormatting.WHITE);
    public Value<Boolean> antiSpam = value(false);
    public Value<Double> antiSpamPrejudice = value(0D);
    public Value<Boolean> unreadFlashing = value(true);
    public Value<Boolean> checkUpdates = value(true);
}
