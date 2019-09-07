package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.MessagePatterns
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.ValueList
import mnm.mods.tabbychat.util.config.ValueObject

class GeneralServerSettings : ValueObject() {

    val channelsEnabled = Value(true)
    val pmEnabled = Value(true)
    val channelPattern = Value(ChannelPatterns.BRACKETS)
    val messegePattern = Value(MessagePatterns.WHISPERS)
    val useDefaultTab = Value(true)
    val ignoredChannels = ValueList<String>()
    val defaultChannel = Value("")
    val channelCommand = Value("")
    val messageCommand = Value("")
}
