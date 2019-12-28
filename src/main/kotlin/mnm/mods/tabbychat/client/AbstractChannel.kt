package mnm.mods.tabbychat.client

import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.api.Channel
import net.minecraft.util.StringUtils

abstract class AbstractChannel(
        /**
         * The alias that is displayed on the channel tab.
         */
        open var alias: String = "",
        open var command: String = "",
        open var isPrefixHidden: Boolean = false,
        prefix: String = ""
) : Channel {

    /**
     * The prefix that is inserted before the input.
     */
    open var prefix: String = prefix
        set(value) {
            field = StringUtils.stripControlCodes(value)
        }

    fun toConfig(config: Config) {
        config.set<String>("name", name)
        config.set<String>("alias", alias)
        config.set<String>("command", command)
        config.set<Boolean>("isPrefixHidden", isPrefixHidden)
        config.set<String>("prefix", prefix)
    }

    companion object {
        fun <T : AbstractChannel> fromConfig(config: Config, factory: (String) -> T): T {
            return factory(config.get("name")).apply {
                alias = config.get("alias")
                command = config.get("command")
                isPrefixHidden = config.get("isPrefixHidden")
                prefix = config.get("prefix")
            }
        }
    }
}


