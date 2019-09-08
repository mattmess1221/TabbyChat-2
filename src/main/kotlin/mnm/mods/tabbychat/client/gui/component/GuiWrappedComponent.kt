package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.ILocation

open class GuiWrappedComponent<T : GuiComponent>(delegate: T) : GuiWrappedEventListener<T>(delegate) {

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

    override var active: Boolean
        get() = delegate.active
        set(enabled) {
            delegate.active = enabled
        }

    override var visible: Boolean
        get() = delegate.visible
        set(visible) {
            delegate.visible = visible
        }

    override var focused: Boolean
        get() = delegate.focused
        set(value) {
            delegate.focused = value
        }

    override fun isValidButton(button: Int): Boolean {
        return delegate.isValidButton(button)
    }

    override var hovered: Boolean
        get() = delegate.hovered
        set(value) {
            delegate.hovered = value
        }

//    override var caption: ITextComponent?
//        get() = delegate.caption
//        set(value) {
//            delegate.caption = value
//        }

    override fun render(x: Int, y: Int, parTicks: Float) {
        delegate.render(x, y, parTicks)
    }

    override fun tick() {
        delegate.tick()
    }

}
