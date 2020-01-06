package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiCheckbox
import kotlin.reflect.KMutableProperty0

/**
 * A gui input for booleans as a checkbox.
 */
class GuiSettingBoolean(setting: KMutableProperty0<Boolean>) : GuiSetting.ValueSetting<Boolean, GuiCheckbox>(setting, GuiCheckbox())
