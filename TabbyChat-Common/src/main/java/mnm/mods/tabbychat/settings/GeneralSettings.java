package mnm.mods.tabbychat.settings;

import mnm.mods.tabbychat.util.TimeStamps;
import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingObject;
import mnm.mods.util.config.SettingValue;
import net.minecraft.util.EnumChatFormatting;

public class GeneralSettings extends SettingObject<GeneralSettings> {

    @Setting
    public SettingValue<Boolean> logChat = value(true);
    @Setting
    public SettingValue<Boolean> splitLog = value(true);
    @Setting
    public SettingValue<Boolean> timestampChat = value(false);
    @Setting
    public SettingValue<TimeStamps> timestampStyle = value(TimeStamps.MILITARYSECONDS);
    @Setting
    public SettingValue<EnumChatFormatting> timestampColor = value(EnumChatFormatting.WHITE);
    @Setting
    public SettingValue<Boolean> antiSpam = value(false);
    @Setting
    public SettingValue<Double> antiSpamPrejudice = value(0D);
    @Setting
    public SettingValue<Boolean> unreadFlashing = value(true);
    @Setting
    public SettingValue<Boolean> checkUpdates = value(true);
    @Setting
    public Spelling spelling = new Spelling();

    public class Spelling extends SettingObject<Spelling> {

        @Setting
        public SettingValue<Boolean> enabled = value(true);
    }
}
