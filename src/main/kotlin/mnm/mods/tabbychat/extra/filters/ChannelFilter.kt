package mnm.mods.tabbychat.extra.filters

import mnm.mods.tabbychat.api.ChannelType
import mnm.mods.tabbychat.api.filters.Filter
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.TabbyChatClient
import java.util.regex.Pattern

class ChannelFilter : Filter {

    override val pattern: Pattern
        get() = TabbyChatClient.serverSettings.general.channelPattern.pattern

    override fun action(event: FilterEvent) {

        val general = TabbyChatClient.serverSettings.general
        if (general.channelsEnabled) {
            val chan = event.matcher.group(1)
            val dest = ChatManager.getChannel(ChannelType.CHAT, chan)
            event.channels.add(dest)
        }

    }
}
