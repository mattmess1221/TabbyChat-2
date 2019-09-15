package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiCheckbox
import mnm.mods.tabbychat.util.config.Value

/**
 * A gui input for booleans as a checkbox.
 */
class GuiSettingBoolean(setting: Value<Boolean>) : GuiSetting.ValueSetting<Boolean, GuiCheckbox>(setting, GuiCheckbox())
