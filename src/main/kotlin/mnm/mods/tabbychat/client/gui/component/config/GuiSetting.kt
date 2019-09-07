package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent
import mnm.mods.tabbychat.client.gui.component.IGuiInput

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
</T> */
abstract class GuiSetting<T> internal constructor() : GuiComponent(), IGuiInput<T> {

    abstract class Wrapped<T, W : GuiComponent>
    internal constructor(val setting: AbstractValue<T>, wrap: W)
        : GuiWrappedComponent<W>(wrap), IGuiInput<T>

    open class GuiSettingWrapped<T, Wrapper>
    internal constructor(setting: AbstractValue<T>, input: Wrapper)
        : Wrapped<T, Wrapper>(setting, input) where Wrapper : GuiComponent, Wrapper : IGuiInput<T> {

        override var value: T
            get() = delegate.value
            set(value) {
                delegate.value = value
            }

        init {
            input.value = setting.value
        }

    }
}
