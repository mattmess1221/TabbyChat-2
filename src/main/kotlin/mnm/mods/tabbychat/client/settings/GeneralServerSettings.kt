package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.AbstractConfigView
import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.ConfigView
import mnm.mods.tabbychat.util.MessagePatterns

class GeneralServerSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {

    var channelsEnabled by defining(true)
    var pmEnabled by defining(true)
    var channelPattern by definingEnum(ChannelPatterns.BRACKETS, ChannelPatterns::class.java)
    var messegePattern by definingEnum(MessagePatterns.WHISPERS, MessagePatterns::class.java)
    var useDefaultTab by defining(true)
    var ignoredChannels by definingList<String>()
    var defaultChannel by defining("")
//    var channelCommand by defining("")
//    var messageCommand by defining("")
}
