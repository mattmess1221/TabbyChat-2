package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiNumericUpDown
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingDouble
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingInt
import mnm.mods.tabbychat.util.config.Value

/**
 * A base gui setting for numbers. It wraps a [GuiNumericUpDown]
 *
 * @param <T>
 * @see GuiSettingDouble
 * @see GuiSettingInt
 */
abstract class GuiSettingNumber<T : Number>
private constructor(setting: Value<T>, input: GuiNumericUpDown<T>)
    : GuiSetting.ValueSetting<T, GuiNumericUpDown<T>>(setting, input) {

    /**
     * Gui setting for integers
     */
    class GuiSettingInt(setting: Value<Int>) : GuiSettingNumber<Int>(setting, GuiNumericUpDown.IntUpDown())

    /**
     * Gui setting for doubles
     */
    class GuiSettingDouble(setting: Value<Double>) : GuiSettingNumber<Double>(setting, GuiNumericUpDown.DoubleUpDown())
}
