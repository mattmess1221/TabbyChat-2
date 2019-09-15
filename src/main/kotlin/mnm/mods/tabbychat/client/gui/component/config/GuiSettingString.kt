package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiText
import mnm.mods.tabbychat.util.config.Value

/**
 * A Gui input that wraps a [GuiText].
 */
class GuiSettingString(setting: Value<String>) : GuiSetting.ValueSetting<String, GuiText>(setting, GuiText())
