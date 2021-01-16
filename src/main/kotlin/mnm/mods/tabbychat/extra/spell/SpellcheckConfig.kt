package mnm.mods.tabbychat.extra.spell

import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.div
import java.nio.file.Path

class SpellcheckConfig(path: Path) : FileConfigView(path / "spellcheck.toml") {
    @Comment("Enables spell checking")
    var enabled by defining(true)

    @Comment("The fallback locale to use when the current locale has no word lists.")
    var fallbackLocale by defining("en_US")

    @Comment("Automatically downloads new word lists for the locale")
    var autoDownload by defining(false)
}