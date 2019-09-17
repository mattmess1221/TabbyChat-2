package mnm.mods.tabbychat.client.extra.filters

import mnm.mods.tabbychat.api.filters.Filter
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.TabbyChatClient
import java.util.regex.Pattern

/**
 * Base class for filters that just need to set the
 */
class MessageFilter : Filter {

    override val pattern: Pattern
        get() {
            val messege = TabbyChatClient.serverSettings.general.messegePattern.value
            val pattern = String.format("(?:%s|%s)", messege.outgoing, messege.incoming)
            return Pattern.compile(pattern)
        }

    override fun action(event: FilterEvent) {

        if (TabbyChatClient.serverSettings.general.pmEnabled.value) {
            // 0 = whole message, 1 = outgoing recipient, 2 = incoming recipient
            var player: String? = event.matcher.group(1)
            // For when it's an incoming message.
            if (player == null) {
                player = event.matcher.group(2)
            }
            val dest = ChatManager.getUserChannel(player!!)
            event.channels.add(dest)
        }
    }
}
