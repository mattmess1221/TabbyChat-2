package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.client.gui.component.GuiCheckbox
import mnm.mods.tabbychat.client.gui.component.config.GuiSetting.GuiSettingWrapped

/**
 * A gui input for booleans as a checkbox.
 *
 * @author Matthew
 */
class GuiSettingBoolean(setting: AbstractValue<Boolean>) : GuiSettingWrapped<Boolean, GuiCheckbox>(setting, GuiCheckbox())
