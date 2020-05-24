package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView
import net.minecraft.util.text.TextFormatting

class GeneralSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {
    @Comment("Logs chat to a file")
    var logChat by defining(true)
    @Comment("When logging, splits the log by channel")
    var splitLog by defining(true)
    @Comment("Prepend chat messages with the timestamp")
    var timestampChat by defining(false)
    @Comment("The style of the timestamp")
    var timestampStyle by definingEnum(TimeStamps.MILITARYSECONDS, TimeStamps::class.java)
    @Comment( "The color of the timestamp")
    var timestampColor by definingRestrictedEnum(TextFormatting.WHITE, TextFormatting::class.java, TextFormatting.values().filter { it.isColor })
    @Comment("Merge duplicate chat messages with the previous message")
    var antiSpam by defining(false)
    @Comment("The percentage (0-1) of similarity between two message to count as spam")
    var antiSpamPrejudice by defining(0.0)
    @Comment("Show flashing notifications when a channel has unread messages")
    var unreadFlashing by defining(true)
    @Comment("Enable update checking (not implemented)")
    var checkUpdates by defining(true)
}
