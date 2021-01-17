package mnm.mods.tabbychat.extra.filters

import com.electronwill.nightconfig.core.CommentedConfig
import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView
import mnm.mods.tabbychat.util.config.FileConfigView
import net.minecraft.util.ResourceLocation
import java.nio.file.Path
import java.util.regex.Pattern

/**
 * Defines the settings used by filters.
 */
class FilterConfig(config: Config = CommentedConfig.inMemory()) : ConfigView(config) {

    @Comment("The friendly name of the filter")
    var name by defining("")

    @Comment("The regex expression used")
    var expression by defining("")

    // destinations
    @Comment("The channels matching messages get sent to")
    var channels by definingList<String>()

    @Comment("Whether matching messages are removed from chat entirely")
    var remove by defining(false)

    @Comment("If true, color codes are not stripped before matched")
    var colors by defining(true)

    @Comment("If true, regex is used for matching")
    var regex by defining(false)

    @Comment("If true, matches will be case-insensitive")
    var ignoreCase by defining(false)

    @Comment("The sound to be played.\n" +
            "Must be the resource location of the sound.")
    var soundName by defining("") {
        validator = {
            it.isNullOrEmpty() || ResourceLocation.tryCreate(it) != null
        }
    }

    constructor(name: String, expr: String = "") : this() {
        this.name = name
        this.expression = expr
    }
}
