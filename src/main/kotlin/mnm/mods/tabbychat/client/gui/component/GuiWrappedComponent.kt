package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.ILocation
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.util.text.ITextComponent

open class GuiWrappedComponent<T : GuiComponent>(val delegate: T) : GuiComponent(), IGuiEventListener by delegate {

    override var location: ILocation
        get() = delegate.location
        set(location) {
            delegate.location = location
        }

    override var parent: GuiPanel?
        get() = delegate.parent
        set(value) {
            delegate.parent = value
        }

    override var minimumSize: Dim
        get() = delegate.minimumSize
        set(size) {
            delegate.minimumSize = size
        }

    override var primaryColor: Color?
        get() = delegate.primaryColor
        set(value) {
            delegate.primaryColor = value
        }

    override var secondaryColor: Color?
        get() = delegate.secondaryColor
        set(value) {
            delegate.secondaryColor = value
        }

    override var isEnabled: Boolean
        get() = delegate.isEnabled
        set(enabled) {
            delegate.isEnabled = enabled
        }

    override var isVisible: Boolean
        get() = delegate.isVisible
        set(visible) {
            delegate.isVisible = visible
        }

    override var caption: ITextComponent?
        get() = delegate.caption
        set(value) {
            delegate.caption = value
        }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        delegate.render(mouseX, mouseY, parTicks)
    }

    override fun tick() {
        delegate.tick()
    }

}
