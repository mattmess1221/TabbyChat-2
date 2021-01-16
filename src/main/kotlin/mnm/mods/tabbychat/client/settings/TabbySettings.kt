package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.div
import java.nio.file.Path

class TabbySettings(parent: Path) : FileConfigView(parent / "tabbychat.toml") {

    @Comment("General settings for TabbyChat")
    val general by child(::GeneralSettings)

    @Comment("Advanced settings. I don't recommend changing any of these.\n" +
            "They're either handled automatically or for advanced users.")
    val advanced by child(::AdvancedSettings)
}
