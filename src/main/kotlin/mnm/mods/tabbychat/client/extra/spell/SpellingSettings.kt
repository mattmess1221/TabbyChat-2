package mnm.mods.tabbychat.client.extra.spell

import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView

class SpellingSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {

    @Comment("Enables spellcheck for chat input")
    var enabled by defining(true)
}