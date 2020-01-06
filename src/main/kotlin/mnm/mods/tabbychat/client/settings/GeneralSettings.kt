package mnm.mods.tabbychat.client.settings

import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.ConfigView
import net.minecraft.util.text.TextFormatting

class GeneralSettings(config: Config) : ConfigView(config) {
    var logChat by defining(true)
    var splitLog by defining(true)
    var timestampChat by defining(false)
    var timestampStyle by definingEnum(TimeStamps.MILITARYSECONDS)
    var timestampColor by definingRestrictedEnum(TextFormatting.WHITE, TextFormatting.values().filter { it.isColor })
    var antiSpam by defining(false)
    var antiSpamPrejudice by defining(0.0)
    var unreadFlashing by defining(true)
    var checkUpdates by defining(true)
}
