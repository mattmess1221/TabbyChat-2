package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.MessagePatterns
import mnm.mods.tabbychat.util.config.ConfigView
import mnm.mods.tabbychat.util.config.FileConfigView

class GeneralServerSettings(config: FileConfigView, path: List<String>) : ConfigView(config, path) {

    val channelsEnabled by defining(true)
    val pmEnabled by defining(true)
    val channelPattern by definingEnum(ChannelPatterns.BRACKETS)
    val messegePattern by definingEnum(MessagePatterns.WHISPERS)
    val useDefaultTab by defining(true)
    val ignoredChannels by definingList(listOf<String>())
    val defaultChannel by defining("")
    val channelCommand by defining("")
    val messageCommand by defining("")
}
