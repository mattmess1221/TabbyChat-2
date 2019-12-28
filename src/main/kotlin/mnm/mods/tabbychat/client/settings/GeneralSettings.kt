package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.config.ConfigView
import mnm.mods.tabbychat.util.config.FileConfigView
import net.minecraft.util.text.TextFormatting

class GeneralSettings(config: FileConfigView, path: List<String>) : ConfigView(config, path) {
    val logChat by defining(true)
    val splitLog by defining(true)
    val timestampChat by defining(false)
    val timestampStyle by definingEnum(TimeStamps.MILITARYSECONDS)
    val timestampColor by definingRestrictedEnum(TextFormatting.WHITE, TextFormatting.values().filter { it.isColor })
    val antiSpam by defining(false)
    val antiSpamPrejudice by defining(0.0)
    val unreadFlashing by defining(true)
    val checkUpdates by defining(true)
}
