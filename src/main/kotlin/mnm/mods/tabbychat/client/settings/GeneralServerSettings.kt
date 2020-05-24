package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.*

class GeneralServerSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {

    @Comment("Enables channel detection on this server")
    var channelsEnabled by defining(true)
    @Comment("Enables direct message detection on this server")
    var pmEnabled by defining(true)
    @Comment("Specifies the hard-coded pattern to support channel detection")
    var channelPattern by definingEnum(ChannelPatterns.BRACKETS, ChannelPatterns::class.java)
    @Comment("Specifies the hard-coded pattern to support direct message detection")
    var messegePattern by definingEnum(MessagePatterns.WHISPERS, MessagePatterns::class.java)
    @Comment("Sends messages which detected as a channel to the default channel (*)")
    var useDefaultTab by defining(true)
    @Comment("The list of ignored channels")
    var ignoredChannels by definingList<String>()
    @Comment("The default channel name (not implemented)")
    var defaultChannel by defining("*")
//    var channelCommand by defining("")
//    var messageCommand by defining("")
}
