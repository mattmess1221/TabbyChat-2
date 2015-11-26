package mnm.mods.tabbychat.settings;

import com.google.gson.annotations.Expose;

import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.config.Value;
import mnm.mods.util.config.ValueObject;
import net.minecraft.util.EnumChatFormatting;

public class GeneralSettings extends ValueObject {

    @Expose
    public Value<Boolean> logChat = value(true);
    @Expose
    public Value<Boolean> splitLog = value(true);
    @Expose
    public Value<Boolean> timestampChat = value(false);
    @Expose
    public Value<TimeStamps> timestampStyle = value(TimeStamps.MILITARYSECONDS);
    @Expose
    public Value<EnumChatFormatting> timestampColor = value(EnumChatFormatting.WHITE);
    @Expose
    public Value<Boolean> antiSpam = value(false);
    @Expose
    public Value<Double> antiSpamPrejudice = value(0D);
    @Expose
    public Value<Boolean> unreadFlashing = value(true);
    @Expose
    public Value<Boolean> checkUpdates = value(true);
    @Expose
    public Spelling spelling = new Spelling();

    public class Spelling extends ValueObject {

        @Expose
        public Value<Boolean> enabled = new Value<Boolean>(true);
    }
}
