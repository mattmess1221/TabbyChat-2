package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.config.ValueObject
import net.minecraft.util.text.TextFormatting

class GeneralSettings : ValueObject<GeneralSettings>() {
    val logChat by value { true }
    val splitLog by value { true }
    val timestampChat by value { false }
    val timestampStyle by value { TimeStamps.MILITARYSECONDS }
    val timestampColor by value { TextFormatting.WHITE }
    val antiSpam by value { false }
    val antiSpamPrejudice by value { 0.0 }
    val unreadFlashing by value { true }
    val checkUpdates by value { true }
}
