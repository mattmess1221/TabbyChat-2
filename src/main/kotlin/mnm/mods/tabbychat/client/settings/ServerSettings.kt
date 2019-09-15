package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.client.ChatChannel
import mnm.mods.tabbychat.client.UserChannel
import mnm.mods.tabbychat.client.extra.filters.UserFilter
import mnm.mods.tabbychat.util.config.SettingsFile
import mnm.mods.tabbychat.util.div
import mnm.mods.tabbychat.util.toPath
import mnm.mods.tabbychat.util.urlEncoded
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.file.Path

class ServerSettings(
        parent: Path,
        socket: SocketAddress
) : SettingsFile<ServerSettings>(parent / socket2path(socket) / "server.json") {

    val general by obj { GeneralServerSettings() }
    val filters by list<UserFilter>(typeToken())
    val channels by map<ChatChannel>(typeToken())
    val pms by map<UserChannel>(typeToken())

    private companion object {
        fun socket2path(addr: SocketAddress): Path {
            return (addr as? InetSocketAddress)?.let {
                "multiplayer" / it.toString().urlEncoded
            } ?: "singleplayer".toPath()
        }
    }
}
