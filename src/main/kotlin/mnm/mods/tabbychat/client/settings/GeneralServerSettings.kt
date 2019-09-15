package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.MessagePatterns
import mnm.mods.tabbychat.util.config.ValueObject

class GeneralServerSettings : ValueObject<GeneralServerSettings>() {

    val channelsEnabled by value { true }
    val pmEnabled by value { true }
    val channelPattern by value { ChannelPatterns.BRACKETS }
    val messegePattern by value { MessagePatterns.WHISPERS }
    val useDefaultTab by value { true }
    val ignoredChannels by list<String>(typeToken())
    val defaultChannel by value { "" }
    val channelCommand by value { "" }
    val messageCommand by value { "" }
}
