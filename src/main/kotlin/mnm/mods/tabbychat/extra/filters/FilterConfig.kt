package mnm.mods.tabbychat.extra.filters

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
class FilterConfig(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {

    // destinations
    @Comment("The channels matching messages get sent to")
    var channels by definingList<String>()

    @Comment("Whether matching messages are removed from chat entirely")
    var isRemove by defining(false)

    @Comment("If true, color codes are not stripped before matched")
    var isRaw by defining(true)

    @Comment("If true, regex is used for matching")
    var isRegex by defining(false)

    @Comment("If true, matches will be case-insensitive")
    var isCaseInsensitive by defining(false)

    @Comment("If true, a sound will be played when matched")
    var isSoundNotification by defining(false)

    @Comment("The sound to be played.\n" +
            "Must be the resource location of the sound.")
    var soundName by defining("") {
        validator = {
            it.isNullOrEmpty() || ResourceLocation.tryCreate(it) != null
        }
    }

    val soundLocation get() = ResourceLocation.tryCreate(soundName)

    val flags: Int
        get() {
            var flags = 0
            if (isCaseInsensitive) {
                flags = flags or Pattern.CASE_INSENSITIVE
            }
            return flags
        }
}
