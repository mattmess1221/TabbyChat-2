package mnm.mods.tabbychat.client.settings

import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.client.AbstractChannel
import mnm.mods.tabbychat.client.ChatChannel
import mnm.mods.tabbychat.client.UserChannel
import mnm.mods.tabbychat.client.extra.filters.UserFilter
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.div
import mnm.mods.tabbychat.util.toPath
import mnm.mods.tabbychat.util.urlEncoded
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.file.Path

class ServerSettings(
        parent: Path,
        socket: SocketAddress
) : FileConfigView(parent / socket2path(socket) / "server.toml") {

    val general by child(::GeneralServerSettings)
    private val filters by definingList(listOf<Config>())
    private val channels by definingList(listOf<Config>())
    private val pms by definingList(listOf<Config>())

    private companion object {
        fun socket2path(addr: SocketAddress): Path {
            return (addr as? InetSocketAddress)?.let {
                "multiplayer" / it.toString().urlEncoded
            } ?: "singleplayer".toPath()
        }
    }

    fun getFilters() = filters.value.map { UserFilter.fromConfig(it) }
    fun setFilters(filters: List<UserFilter>) {
        this.filters.value = filters.map {
            config.createSubConfig().also { cfg -> it.toConfig(cfg) }
        }
    }

    fun getChannels() = channels.value.map { AbstractChannel.fromConfig(it, ::ChatChannel) }
    fun setChannels(channels: List<ChatChannel>) {
        this.channels.value = channels.map {
            config.createSubConfig().also { cfg -> it.toConfig(cfg) }
        }
    }

    fun getPms() = pms.value.map { AbstractChannel.fromConfig(it, ::UserChannel) }
    fun setPms(pms: List<UserChannel>) {
        this.pms.value = pms.map {
            config.createSubConfig().also { cfg -> it.toConfig(cfg) }
        }
    }
}
