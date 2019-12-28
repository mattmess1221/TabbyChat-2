package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent
import mnm.mods.tabbychat.client.gui.component.IGuiInput
import mnm.mods.tabbychat.util.config.Spec

/**
 * A base setting gui wrapper.
 *
 * @author Matthew
 * @param <T> The setting type
 */
interface GuiSetting<T : Any> : IGuiInput<T> {

    abstract class GuiSettingWrapped<T : Any, W>
    internal constructor(setting: Spec<T>, wrap: W)
        : GuiWrappedComponent<W>(wrap), GuiSetting<T>, IGuiInput<T>
            where W : GuiComponent, W : IGuiInput<T> {
        final override var value by setting

        init {
            delegate.value = this.value
        }

        override fun tick() {
            value = delegate.value
        }
    }

    abstract class ValueSetting<T : Any, W>
    internal constructor(setting: Spec<T>, wrap: W) : GuiSettingWrapped<T, W>(setting, wrap)
            where W : GuiComponent, W : IGuiInput<T>

    abstract class ListSetting<T : Any, W>
    internal constructor(setting: Spec<List<T>>, wrap: W) : ValueSetting<List<T>, W>(setting, wrap)
            where W : GuiComponent, W : IGuiInput<List<T>>
}
