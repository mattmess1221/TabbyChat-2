package mnm.mods.tabbychat.client.gui.component.config;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import mnm.mods.tabbychat.util.config.ValueList;
import mnm.mods.tabbychat.client.gui.component.GuiText;
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent;
import mnm.mods.tabbychat.client.gui.component.IGuiInput;
import mnm.mods.tabbychat.client.gui.component.config.GuiSetting.GuiSettingWrapped;

import java.util.List;

public class GuiSettingStringList extends GuiSettingWrapped<List<String>, GuiSettingStringList.GuiStringList> {

    public GuiSettingStringList(ValueList<String> setting, String split, String join) {
        super(setting, new GuiStringList(split, join));
    }

    public GuiSettingStringList(ValueList<String> setting, String split) {
        this(setting, split, split);
    }

    public GuiSettingStringList(ValueList<String> setting) {
        this(setting, ",", ", ");
    }

    public static class GuiStringList extends GuiWrappedComponent<GuiText> implements IGuiInput<List<String>> {

        private String split;
        private String join;

        public GuiStringList(String split, String join) {
            super(new GuiText());
            this.split = split;
            this.join = join;
        }

        @Override
        public List<String> getValue() {
            return Splitter.on(split).omitEmptyStrings().trimResults().splitToList(getComponent().getValue());
        }

        @Override
        public void setValue(List<String> value) {
            getComponent().setValue(Joiner.on(join).skipNulls().join(value));
        }

        @Deprecated
        public GuiText getText() {
            return getComponent();
        }
    }
}
