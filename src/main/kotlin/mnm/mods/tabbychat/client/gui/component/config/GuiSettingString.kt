package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiText
import mnm.mods.tabbychat.util.config.Spec

/**
 * A Gui input that wraps a [GuiText].
 */
class GuiSettingString(setting: Spec<String>) : GuiSetting.ValueSetting<String, GuiText>(setting, GuiText())
