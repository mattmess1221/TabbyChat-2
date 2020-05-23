package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.client.ChannelImpl
import mnm.mods.tabbychat.client.extra.filters.UserFilter
import mnm.mods.tabbychat.util.FileConfigView
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
    val filters by childList(::UserFilter)
    val channels by childList(::ChannelImpl)

    private companion object {
        fun socket2path(addr: SocketAddress): Path {
            return (addr as? InetSocketAddress)?.let {
                "multiplayer" / it.toString().urlEncoded
            } ?: "singleplayer".toPath()
        }
    }
}
