package mnm.mods.tabbychat.client

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
}


