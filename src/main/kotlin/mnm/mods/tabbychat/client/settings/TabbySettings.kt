package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.extra.log.ChatLoggingConfig
import mnm.mods.tabbychat.extra.spam.ChatAntiSpamConfig
import mnm.mods.tabbychat.extra.spell.SpellcheckConfig
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.div
import java.nio.file.Path

class TabbySettings(parent: Path) : FileConfigView(parent / "tabbychat.toml") {

    @Comment("General settings for TabbyChat")
    val general by child(::GeneralSettings)

    @Comment("Spellchecking settings")
    val spellcheck by child(::SpellcheckConfig)

    @Comment("Anti-Spam settings")
    val antispam by child(::ChatAntiSpamConfig)

    @Comment("Chat logging settings)")
    val logging by child(::ChatLoggingConfig)

    @Comment("Advanced settings. I don't recommend changing any of these.\n" +
            "They're either handled automatically or for advanced users.")
    val advanced by child(::AdvancedSettings)
}
