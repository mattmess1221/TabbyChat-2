package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiText
import kotlin.reflect.KMutableProperty0

/**
 * A Gui input that wraps a [GuiText].
 */
class GuiSettingString(setting: KMutableProperty0<String>) : GuiSetting.ValueSetting<String, GuiText>(setting, GuiText())
