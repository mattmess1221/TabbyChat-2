package mnm.mods.tabbychat.extra.filters

import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.util.config.ConfigManager
import mnm.mods.tabbychat.util.div
import mnm.mods.tabbychat.util.mc
import mnm.mods.tabbychat.util.urlEncoded
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.file.Files
import java.nio.file.Path
import java.util.regex.Pattern
import kotlin.streams.toList

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

    private val filters = mutableListOf<UserFilter>()

    init {
        ConfigManager.addConfigs { filters }
    }

    private fun findFilter(name: String) = filters.find {
        it.config.nioPath.endsWith("$name.toml")
    }

    private fun findServerFilters(server: SocketAddress): List<UserFilter> {
        val path = socket2path(server)
        Files.createDirectories(path)
        return Files.list(path)
                .filter { it.endsWith(".toml") }
                .map { UserFilter(it) }
                .toList()
    }

    fun createFilter(server: SocketAddress, name: String): UserFilter {
        require(findFilter(name) == null) { "Config with name: '$name' already exists" }
        val path = socket2path(server)

        val config = UserFilter(path / "$name.toml")
        filters += config
        return config
    }

    private fun filters() = (builtinFilters + filters).asIterable()

    private fun socket2path(addr: SocketAddress): Path {
        return "server" / ((addr as? InetSocketAddress)?.toString()?.urlEncoded ?: "singleplayer")
    }

    @SubscribeEvent
    fun onChatRecieved(message: ChatReceivedEvent) {
        // We're possibly not in game.
        if (mc.connection == null) return

        val text = message.text
        if (text != null) {

            for (filter in filters()) {

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

    @SubscribeEvent
    fun onLogIn(event: ClientPlayerNetworkEvent.LoggedInEvent) {
        filters.clear()
        filters += findServerFilters(event.networkManager!!.remoteAddress)
    }

    @SubscribeEvent
    fun onLogOut(event: ClientPlayerNetworkEvent.LoggedOutEvent) {
        filters.clear()
    }
}

