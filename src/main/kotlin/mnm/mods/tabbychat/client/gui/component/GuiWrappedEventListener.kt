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

    override fun mouseScrolled(p_mouseScrolled_1_: Double, p_mouseScrolled_3_: Double, p_mouseScrolled_5_: Double): Boolean {
        return delegate.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_)
    }


    override fun keyPressed(p_keyPressed_1_: Int, p_keyPressed_2_: Int, p_keyPressed_3_: Int): Boolean {
        return delegate.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return delegate.keyReleased(keyCode, scanCode, modifiers)
    }

    override fun charTyped(p_charTyped_1_: Char, p_charTyped_2_: Int): Boolean {
        return delegate.charTyped(p_charTyped_1_, p_charTyped_2_)
    }

    override fun changeFocus(focus: Boolean): Boolean {
        return delegate.changeFocus(focus)
    }

    override fun isMouseOver(x: Double, y: Double): Boolean {
        return delegate.isMouseOver(x, y)
    }
}