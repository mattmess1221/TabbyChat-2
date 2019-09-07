package mnm.mods.tabbychat.client.gui.component

import net.minecraft.client.gui.IGuiEventListener

@Deprecated("Use a kotlin delegate type")
interface IGuiEventListenerDelegate : IGuiEventListener  {

    @Deprecated("Use a kotlin delegate type")
    fun delegate(): IGuiEventListener

    override fun mouseMoved(p_212927_1_: Double, p_212927_3_: Double) {
        delegate().mouseMoved(p_212927_1_, p_212927_3_)
    }

    override fun mouseClicked(p_mouseClicked_1_: Double, p_mouseClicked_3_: Double, p_mouseClicked_5_: Int): Boolean {
        return delegate().mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)
    }

    override fun mouseReleased(p_mouseReleased_1_: Double, p_mouseReleased_3_: Double, p_mouseReleased_5_: Int): Boolean {
        return delegate().mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_)
    }

    override fun mouseDragged(p_mouseDragged_1_: Double, p_mouseDragged_3_: Double, p_mouseDragged_5_: Int, p_mouseDragged_6_: Double, p_mouseDragged_8_: Double): Boolean {
        return delegate().mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_)
    }

    override fun mouseScrolled(p_mouseScrolled_1_: Double, p_mouseScrolled_3_: Double, p_mouseScrolled_5_: Double): Boolean {
        return delegate().mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_)
    }

    override fun keyPressed(p_keyPressed_1_: Int, p_keyPressed_2_: Int, p_keyPressed_3_: Int): Boolean {
        return delegate().keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_)
    }

    override fun keyReleased(p_keyReleased_1_: Int, p_keyReleased_2_: Int, p_keyReleased_3_: Int): Boolean {
        return delegate().keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_)
    }

    override fun charTyped(p_charTyped_1_: Char, p_charTyped_2_: Int): Boolean {
        return delegate().charTyped(p_charTyped_1_, p_charTyped_2_)
    }

    override fun changeFocus(p_changeFocus_1_: Boolean): Boolean {
        return delegate().changeFocus(p_changeFocus_1_)
    }

    override fun isMouseOver(p_isMouseOver_1_: Double, p_isMouseOver_3_: Double): Boolean {
        return delegate().isMouseOver(p_isMouseOver_1_, p_isMouseOver_3_)
    }
}
