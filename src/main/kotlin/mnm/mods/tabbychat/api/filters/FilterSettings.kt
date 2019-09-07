package mnm.mods.tabbychat.api.filters

import java.util.regex.Pattern

/**
 * Defines the settings used by filters.
 */
class FilterSettings {

    // destinations
    val channels: MutableSet<String> = mutableSetOf()
    var isRemove: Boolean = false
    var isRaw = true
    var isRegex: Boolean = false
    var flags: Int = 0
        private set

    // notifications
    var isSoundNotification = false
    var soundName: String? = null

    var isCaseInsensitive: Boolean
        get() = getFlag(Pattern.CASE_INSENSITIVE)
        set(value) = setFlag(Pattern.CASE_INSENSITIVE, value)

    private fun setFlag(flag: Int, value: Boolean) {
        if (value) {
            this.flags = this.flags or flag
        } else {
            this.flags = this.flags and flag.inv()
        }
    }

    private fun getFlag(flag: Int): Boolean {
        return flags and flag != 0
    }
}
