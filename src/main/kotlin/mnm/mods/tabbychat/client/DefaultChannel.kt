package mnm.mods.tabbychat.client

import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.ChannelType

object DefaultChannel : Channel {
    override val name = ""
    override val type = ChannelType.ROOT
    override var alias = ""

    override fun toString() = "*"
}