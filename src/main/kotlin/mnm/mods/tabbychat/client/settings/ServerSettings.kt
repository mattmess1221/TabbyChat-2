package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.client.ChatChannel
import mnm.mods.tabbychat.client.UserChannel
import mnm.mods.tabbychat.client.extra.filters.UserFilter
import mnm.mods.tabbychat.util.config.SettingsFile
import mnm.mods.tabbychat.util.config.ValueList
import mnm.mods.tabbychat.util.config.ValueMap
import mnm.mods.tabbychat.util.div
import mnm.mods.tabbychat.util.toPath
import mnm.mods.tabbychat.util.urlEncoded
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.file.Path

class ServerSettings(
        parent: Path,
        val socket: SocketAddress
) : SettingsFile<ServerSettings>(parent / socket2path(socket) / "server.json") {

    val general: GeneralServerSettings by obj { GeneralServerSettings() }
    val filters: ValueList<UserFilter> by list()
    val channels: ValueMap<ChatChannel> by map()
    val pms: ValueMap<UserChannel> by map()

    private companion object {
        fun socket2path(addr: SocketAddress): Path {
            return (addr as? InetSocketAddress)?.let {
                "multiplayer" / it.toString().urlEncoded
            } ?: "singleplayer".toPath()
        }
    }
}
