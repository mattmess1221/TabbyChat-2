package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.FileConfigView
import mnm.mods.tabbychat.util.div

import java.nio.file.Path

class TabbySettings(parent: Path) : FileConfigView(parent / "tabbychat.toml") {

    val general by child(::GeneralSettings)
    val advanced by child(::AdvancedSettings) {
        "Advanced settings. I don't recommend changing any of these. They're all handled automatically."
    }
}
