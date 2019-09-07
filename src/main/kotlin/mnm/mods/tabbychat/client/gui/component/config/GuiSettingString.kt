package mnm.mods.tabbychat.client.gui.component.config;

import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.client.gui.component.GuiText;
import mnm.mods.tabbychat.client.gui.component.config.GuiSetting.GuiSettingWrapped;

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
