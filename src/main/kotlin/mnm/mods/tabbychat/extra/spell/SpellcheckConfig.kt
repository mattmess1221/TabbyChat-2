package mnm.mods.tabbychat.extra.spell

import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView

class SpellcheckConfig(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {
    @Comment("Enables spell checking")
    var enabled by defining(true)

    @Comment("The fallback locale to use when the current locale has no word lists.")
    var fallbackLocale by defining("en_US")

    @Comment("Automatically downloads new word lists for the locale")
    var autoDownload by defining(false)
}