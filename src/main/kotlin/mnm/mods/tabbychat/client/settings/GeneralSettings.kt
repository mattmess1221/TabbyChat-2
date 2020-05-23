package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.AbstractConfigView
import mnm.mods.tabbychat.util.ConfigView
import mnm.mods.tabbychat.util.TimeStamps
import net.minecraft.util.text.TextFormatting

class GeneralSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {
    var logChat by defining(true)
    var splitLog by defining(true)
    var timestampChat by defining(false)
    var timestampStyle by definingEnum(TimeStamps.MILITARYSECONDS, TimeStamps::class.java)
    var timestampColor by definingRestrictedEnum(TextFormatting.WHITE, TextFormatting::class.java, TextFormatting.values().filter { it.isColor })
    var antiSpam by defining(false)
    var antiSpamPrejudice by defining(0.0)
    var unreadFlashing by defining(true)
    var checkUpdates by defining(true)
}
