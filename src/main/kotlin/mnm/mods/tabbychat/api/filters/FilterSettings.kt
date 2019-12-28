package mnm.mods.tabbychat.api.filters

import java.util.regex.Pattern

/**
 * Defines the settings used by filters.
 */
class FilterSettings {

    // destinations
    var channels = listOf<String>()
    var isRemove = false
    var isRaw = true
    var isRegex = false
    var isCaseInsensitive = false

    // notifications
    var isSoundNotification = false
    var soundName: String? = null

    val flags: Int
        get() {
            var flags = 0
            if (isCaseInsensitive) {
                flags = flags or Pattern.CASE_INSENSITIVE
            }
            return flags
        }
}
