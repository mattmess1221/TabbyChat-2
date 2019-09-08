package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.MessagePatterns
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.ValueList
import mnm.mods.tabbychat.util.config.ValueObject

class GeneralServerSettings : ValueObject<GeneralServerSettings>() {

    val channelsEnabled: Value<Boolean> by value { true }
    val pmEnabled: Value<Boolean> by value { true }
    val channelPattern: Value<ChannelPatterns> by value { ChannelPatterns.BRACKETS }
    val messegePattern: Value<MessagePatterns> by value { MessagePatterns.WHISPERS }
    val useDefaultTab: Value<Boolean> by value { true }
    val ignoredChannels: ValueList<String> by list<String>()
    val defaultChannel: Value<String> by value { "" }
    val channelCommand: Value<String> by value { "" }
    val messageCommand: Value<String> by value { "" }
}
