package mnm.mods.tabbychat.client

import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.ChannelType
import mnm.mods.tabbychat.util.ConfigView
import net.minecraft.util.StringUtils

class ChannelImpl(config: Config = Config.inMemory()) : ConfigView(config), Channel {

    override var name by defining("")
    override var type by definingEnum(ChannelType.OTHER, ChannelType::class.java)
        private set
    override var alias by defining("")

    var command by defining("")
    var isPrefixHidden by defining(false)
    var prefix by defining("") {
        serialize = {
            StringUtils.stripControlCodes(it)
        }
        deserialize = { value ->
            value?.let { StringUtils.stripControlCodes(it) } ?: default
        }
    }

    constructor(type: ChannelType, name: String) : this() {
        this.name = name
        this.type = type
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChannelImpl

        if (name != other.name) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    override fun toString() = type.prefix + name
}

object DefaultChannel : Channel {
    override val name = ""
    override val type = ChannelType.ROOT
    override var alias = ""

    override fun toString() = "*"
}
