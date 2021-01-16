package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView
import net.minecraft.util.text.TextFormatting

class GeneralSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {
    @Comment("Prepend chat messages with the timestamp")
    var timestampChat by defining(false)

    @Comment("The style of the timestamp")
    var timestampStyle by definingEnum(TimeStamps.MILITARYSECONDS, TimeStamps::class.java)

    @Comment("The color of the timestamp")
    var timestampColor by definingRestrictedEnum(TextFormatting.WHITE, TextFormatting::class.java, TextFormatting.values().filter { it.isColor })

    @Comment("Show flashing notifications when a channel has unread messages")
    var unreadFlashing by defining(true)

    @Comment("Enable update checking (not implemented)")
    var checkUpdates by defining(true)
}
