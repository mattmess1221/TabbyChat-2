package mnm.mods.util.gui.config;

import mnm.mods.util.config.Value;
import mnm.mods.util.gui.GuiCheckbox;
import mnm.mods.util.gui.config.GuiSetting.GuiSettingWrapped;

/**
 * A gui input for booleans as a checkbox.
 *
 * @author Matthew
 */
public class GuiSettingBoolean extends GuiSettingWrapped<Boolean, GuiCheckbox> {

    public GuiSettingBoolean(Value<Boolean> setting) {
        super(setting, new GuiCheckbox());
    }
}
