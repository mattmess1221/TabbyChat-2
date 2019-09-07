package mnm.mods.tabbychat.client.extra.filters

import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.mc
import net.minecraftforge.eventbus.api.SubscribeEvent
import java.util.regex.Pattern

object FilterAddon {

    private val filters = listOf(ChannelFilter(), MessageFilter())

    val variables = mapOf(
            "player" to { Pattern.quote(mc.session.username) },
            "onlineplayer" to {
                mc.connection!!.playerInfoMap.joinToString("|", "(?:", ")") {
                    Pattern.quote(it.gameProfile.name)
                }
            }
    )


    @SubscribeEvent
    fun onChatRecieved(message: ChatReceivedEvent) {
        // We're possibly not in game.
        val settings = TabbyChatClient.serverSettings ?: return

        val text = message.text
        if (text != null) {

            for (filter in filters + settings.filters) {

                val matcher = filter.pattern.matcher(filter.prepareText(text))
                val event = FilterEvent(matcher, message.channels, text)
                while (matcher.find()) {
                    filter.action(event)
                }
                message.text = event.text // Set the new chat
                message.channels = event.channels // Add new channels.
            }
        }
    }


}

