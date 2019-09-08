package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.util.config.ValueObject
import net.minecraft.util.text.TextFormatting

class GeneralSettings : ValueObject<GeneralSettings>() {
    val logChat: Value<Boolean> by value { true }
    val splitLog: Value<Boolean> by value { true }
    val timestampChat: Value<Boolean> by value { false }
    val timestampStyle: Value<TimeStamps> by value { TimeStamps.MILITARYSECONDS }
    val timestampColor: Value<TextFormatting> by value { TextFormatting.WHITE }
    val antiSpam: Value<Boolean> by value { false }
    val antiSpamPrejudice: Value<Double> by value { 0.0 }
    val unreadFlashing: Value<Boolean> by value { true }
    val checkUpdates: Value<Boolean> by value { true }
}
