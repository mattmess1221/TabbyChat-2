package mnm.mods.tabbychat.client.settings

import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.MessagePatterns
import mnm.mods.tabbychat.util.ConfigView

class GeneralServerSettings(config: Config) : ConfigView(config) {

    var channelsEnabled by defining(true)
    var pmEnabled by defining(true)
    var channelPattern by definingEnum(ChannelPatterns.BRACKETS)
    var messegePattern by definingEnum(MessagePatterns.WHISPERS)
    var useDefaultTab by defining(true)
    var ignoredChannels by definingList<String>()
    var defaultChannel by defining("")
//    var channelCommand by defining("")
//    var messageCommand by defining("")
}
