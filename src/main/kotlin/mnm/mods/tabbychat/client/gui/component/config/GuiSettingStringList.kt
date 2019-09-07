package mnm.mods.tabbychat.client.gui.component.config

import com.google.common.base.Joiner
import com.google.common.base.Splitter
import mnm.mods.tabbychat.util.config.ValueList
import mnm.mods.tabbychat.client.gui.component.GuiText
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent
import mnm.mods.tabbychat.client.gui.component.IGuiInput
import mnm.mods.tabbychat.client.gui.component.config.GuiSetting.GuiSettingWrapped

class GuiSettingStringList(
        setting: ValueList<String>,
        split: String,
        join: String = split)
    : GuiSettingWrapped<MutableList<String>, GuiSettingStringList.GuiStringList>(setting, GuiStringList(split, join)) {

    constructor(setting: ValueList<String>) : this(setting, ",", ", ")

    class GuiStringList(
            private val split: String,
            private val join: String)
        : GuiWrappedComponent<GuiText>(GuiText()), IGuiInput<MutableList<String>> {

        override var value: MutableList<String>
            get() = Splitter.on(split).omitEmptyStrings().trimResults().splitToList(delegate.value)
            set(value) {
                delegate.value = Joiner.on(join).skipNulls().join(value)
            }

        val text: GuiText
            @Deprecated("")
            get() = delegate
    }
}
