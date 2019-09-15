package mnm.mods.tabbychat.client.gui.component

import net.minecraft.client.gui.IGuiEventListener

open class GuiWrappedEventListener<T : IGuiEventListener>(val delegate: T) : GuiComponent() {

    override fun mouseMoved(xPos: Double, p_212927_3_: Double) {
        delegate.mouseMoved(xPos, p_212927_3_)
    }

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        return delegate.mouseClicked(x, y, button)
    }

    override fun mouseReleased(x: Double, y: Double, button: Int): Boolean {
        return delegate.mouseReleased(x, y, button)
    }

    override fun mouseDragged(x: Double, y: Double, button: Int, dx: Double, dy: Double): Boolean {
        return delegate.mouseDragged(x, y, button, dx, dy)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        return delegate.mouseScrolled(x, y, scroll)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return delegate.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return delegate.keyReleased(keyCode, scanCode, modifiers)
    }

    override fun charTyped(code: Char, modifiers: Int): Boolean {
        return delegate.charTyped(code, modifiers)
    }

    override fun changeFocus(focus: Boolean): Boolean {
        return delegate.changeFocus(focus)
    }

    override fun isMouseOver(x: Double, y: Double): Boolean {
        return delegate.isMouseOver(x, y)
    }
}