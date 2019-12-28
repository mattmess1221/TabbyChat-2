package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiNumericUpDown
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingDouble
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingInt
import mnm.mods.tabbychat.util.config.Spec

/**
 * A base gui setting for numbers. It wraps a [GuiNumericUpDown]
 *
 * @param <T>
 * @see GuiSettingDouble
 * @see GuiSettingInt
 */
abstract class GuiSettingNumber<T : Number>
private constructor(setting: Spec<T>, input: GuiNumericUpDown<T>)
    : GuiSetting.ValueSetting<T, GuiNumericUpDown<T>>(setting, input) {

    /**
     * Gui setting for integers
     */
    class GuiSettingInt(setting: Spec<Int>) : GuiSettingNumber<Int>(setting, GuiNumericUpDown.IntUpDown())

    /**
     * Gui setting for doubles
     */
    class GuiSettingDouble(setting: Spec<Double>) : GuiSettingNumber<Double>(setting, GuiNumericUpDown.DoubleUpDown())
}
