package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.util.config.ValueObject
import net.minecraft.util.text.TextFormatting

class GeneralSettings : ValueObject() {

    val logChat = Value(true)
    val splitLog = Value(true)
    val timestampChat = Value(false)
    val timestampStyle = Value(TimeStamps.MILITARYSECONDS)
    val timestampColor = Value(TextFormatting.WHITE)
    val antiSpam = Value(false)
    val antiSpamPrejudice = Value(0.0)
    val unreadFlashing = Value(true)
    val checkUpdates = Value(true)
}
