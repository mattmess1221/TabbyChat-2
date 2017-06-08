package mnm.mods.util.gui.config;

import mnm.mods.util.config.Value;
import mnm.mods.util.gui.GuiText;
import mnm.mods.util.gui.config.GuiSetting.GuiSettingWrapped;

/**
 * A Gui input that wraps a {@link GuiText}.
 *
 * @author Matthew
 */
public class GuiSettingString extends GuiSettingWrapped<String, GuiText> {

    public GuiSettingString(Value<String> setting) {
        super(setting, new GuiText());
    }

}
