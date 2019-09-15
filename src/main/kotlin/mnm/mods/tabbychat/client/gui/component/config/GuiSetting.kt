package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent
import mnm.mods.tabbychat.client.gui.component.IGuiInput
import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.ValueList

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
 */
interface GuiSetting<V : AbstractValue<T>, T> : IGuiInput<T> {

    val setting: V

    abstract class GuiSettingWrapped<V : AbstractValue<T>, T, W>
    internal constructor(final override val setting: V, wrap: W)
        : GuiWrappedComponent<W>(wrap), GuiSetting<V, T>, IGuiInput<T>
            where W : GuiComponent, W : IGuiInput<T> {

        init {
            delegate.value = setting.value
        }

        override fun tick() {
            value = delegate.value
        }
    }

    abstract class ValueSetting<T, W>
    internal constructor(setting: Value<T>, wrap: W)
        : GuiSettingWrapped<Value<T>, T, W>(setting, wrap)
            where W : GuiComponent, W : IGuiInput<T> {
        override var value: T
            get() = setting.value
            set(value) {
                setting.value = value
            }
    }

    abstract class ListSetting<T, W>
    internal constructor(setting: ValueList<T>, wrap: W)
        : GuiSettingWrapped<ValueList<T>, MutableList<T>, W>(setting, wrap)
            where W : GuiComponent, W : IGuiInput<MutableList<T>> {
        final override var value: MutableList<T>
            get() = setting.value
            set(value) {
                setting.clear()
                setting.addAll(value)
            }
    }
}
