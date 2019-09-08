package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.config.SettingsFile
import mnm.mods.tabbychat.util.div

import java.nio.file.Path

class TabbySettings(parent: Path) : SettingsFile<TabbySettings>(parent / "tabbychat.json") {

    val general by obj { GeneralSettings() }
    val advanced by obj { AdvancedSettings() }
}
