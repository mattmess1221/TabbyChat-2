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
        @field:Transient val socket: SocketAddress
) : SettingsFile(parent / socket2path(socket) / "server.json") {

    val general = GeneralServerSettings()
    val filters = ValueList<UserFilter>()
    val channels = ValueMap<ChatChannel>()
    val pms = ValueMap<UserChannel>()

    private companion object {
        fun socket2path(addr: SocketAddress): Path {
            return (addr as? InetSocketAddress)?.let {
                "multiplayer" / it.toString().urlEncoded
            } ?: "singleplayer".toPath()
        }
    }
}
