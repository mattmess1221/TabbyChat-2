package mnm.mods.tabbychat.client.gui.component.config;

import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.client.gui.component.GuiCheckbox;
import mnm.mods.tabbychat.client.gui.component.config.GuiSetting.GuiSettingWrapped;

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
