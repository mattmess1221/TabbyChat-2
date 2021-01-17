package mnm.mods.tabbychat.extra.filters

import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.mc
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.util.regex.Pattern

@Mod.EventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
object FilterAddon {

    private val builtinFilters = listOf(ChannelFilter(), MessageFilter())

    val variables = mapOf(
            "player" to { Pattern.quote(mc.session.username) },
            "onlineplayer" to {
                mc.connection!!.playerInfoMap.joinToString("|", "(?:", ")") {
                    Pattern.quote(it.gameProfile.name)
                }
            }
    )

    private val filters = mutableMapOf<String, UserFilter>()

    fun createFilter(name: String): UserFilter {
        require(name !in filters) { "Config with name: '$name' already exists" }

        val config = UserFilter(FilterConfig(name))

        filters[name] = config
        TabbyChatClient.serverSettings.filters.add(config.config)

        return config
    }

    private fun filters() = (builtinFilters + filters.values).asIterable()

    @SubscribeEvent
    fun onChatRecieved(message: ChatReceivedEvent) {
        // We're possibly not in game.
        if (mc.connection == null) return

        val text = message.text
        if (text != null) {

            for (filter in filters()) {


                val matcher = filter.expression.find(filter.prepareText(text))
                if (matcher != null) {
                    val event = FilterEvent(matcher, message.channels, text)
                    filter.action(event)
                    message.text = event.text // Set the new chat
                    message.channels = event.channels // Add new channels.
                }
            }
        }
    }

    @SubscribeEvent
    fun onLogIn(event: ClientPlayerNetworkEvent.LoggedInEvent) {
        filters.clear()
        filters += TabbyChatClient.serverSettings.filters.map {
            it.name to UserFilter(it)
        }
    }

    @SubscribeEvent
    fun onLogOut(event: ClientPlayerNetworkEvent.LoggedOutEvent) {
        filters.clear()
    }
}

