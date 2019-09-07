package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiText
import mnm.mods.tabbychat.client.gui.component.config.GuiSetting.GuiSettingWrapped
import mnm.mods.tabbychat.util.config.Value

/**
 * A Gui input that wraps a [GuiText].
 *
 * @author Matthew
 */
class GuiSettingString(setting: Value<String>) : GuiSettingWrapped<String, GuiText>(setting, GuiText())
