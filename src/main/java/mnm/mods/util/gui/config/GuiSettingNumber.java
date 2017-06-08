package mnm.mods.util.gui.config;

import mnm.mods.util.config.Value;
import mnm.mods.util.gui.GuiNumericUpDown;
import mnm.mods.util.gui.config.GuiSetting.GuiSettingWrapped;

/**
 * A base gui setting for numbers. It wraps a {@link GuiNumericUpDown}
 *
 * @author Matthew
 * @param <T>
 * @see GuiSettingDouble
 * @see GuiSettingInt
 */
public abstract class GuiSettingNumber<T extends Number> extends GuiSettingWrapped<T, GuiNumericUpDown<T>> {

    private GuiSettingNumber(Value<T> setting, GuiNumericUpDown<T> input) {
        super(setting, input);
    }

    /**
     * Gui setting for integers
     *
     * @author Matthew
     */
    public static class GuiSettingInt extends GuiSettingNumber<Integer> {

        public GuiSettingInt(Value<Integer> setting) {
            super(setting, new GuiNumericUpDown.IntUpDown());
        }
    }

    /**
     * Gui setting for doubles
     *
     * @author Matthew
     */
    public static class GuiSettingDouble extends GuiSettingNumber<Double> {

        public GuiSettingDouble(Value<Double> setting) {
            super(setting, new GuiNumericUpDown.DoubleUpDown());
        }
    }
}
