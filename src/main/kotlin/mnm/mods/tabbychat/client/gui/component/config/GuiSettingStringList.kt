package mnm.mods.tabbychat.client.gui.component.config

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import mnm.mods.tabbychat.client.gui.component.GuiText
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent
import mnm.mods.tabbychat.client.gui.component.IGuiInput
import mnm.mods.tabbychat.util.config.Spec

class GuiSettingStringList(
        setting: Spec<List<String>>,
        split: String,
        join: String = split)
    : GuiSetting.ListSetting<String, GuiSettingStringList.GuiStringList>(setting, GuiStringList(split, join)) {

    constructor(setting: Spec<List<String>>) : this(setting, ",", ", ")

    class GuiStringList(
            private val split: String,
            private val join: String)
        : GuiWrappedComponent<GuiText>(GuiText()), IGuiInput<List<String>> {

        override var value: List<String>
            get() = Splitter.on(split).omitEmptyStrings().trimResults().splitToList(delegate.value)
            set(value) {
                delegate.value = Joiner.on(join).skipNulls().join(value)
            }
    }
}
