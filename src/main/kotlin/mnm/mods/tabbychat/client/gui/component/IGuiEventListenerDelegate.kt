package mnm.mods.tabbychat.client.gui.component;

import net.minecraft.client.gui.IGuiEventListener;

public interface IGuiEventListenerDelegate extends IGuiEventListener {

    IGuiEventListener delegate();

    @Override
    default void mouseMoved(double p_212927_1_, double p_212927_3_) {
        delegate().mouseMoved(p_212927_1_, p_212927_3_);
    }

    @Override
    default boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        return delegate().mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
    }

    @Override
    default boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        return delegate().mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
    }

    @Override
    default boolean mouseDragged(double p_mouseDragged_1_, double p_mouseDragged_3_, int p_mouseDragged_5_, double p_mouseDragged_6_, double p_mouseDragged_8_) {
        return delegate().mouseDragged(p_mouseDragged_1_, p_mouseDragged_3_, p_mouseDragged_5_, p_mouseDragged_6_, p_mouseDragged_8_);
    }

    @Override
    default boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {
        return delegate().mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_);
    }

    @Override
    default boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        return delegate().keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }

    @Override
    default boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
        return delegate().keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);
    }

    @Override
    default boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
        return delegate().charTyped(p_charTyped_1_, p_charTyped_2_);
    }

    @Override
    default boolean changeFocus(boolean p_changeFocus_1_) {
        return delegate().changeFocus(p_changeFocus_1_);
    }

    @Override
    default boolean isMouseOver(double p_isMouseOver_1_, double p_isMouseOver_3_) {
        return delegate().isMouseOver(p_isMouseOver_1_, p_isMouseOver_3_);
    }
}
