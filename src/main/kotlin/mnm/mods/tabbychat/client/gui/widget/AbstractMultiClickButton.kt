package mnm.mods.tabbychat.client.gui.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.Widget

/**
 * A button that responds to multiple clicks
 */
abstract class AbstractMultiClickButton(x: Int, y: Int, w: Int, h: Int, msg: String) : Widget(x, y, w, h, msg) {

    abstract val validClicks: Set<Int>

    abstract fun onCLick(button: Int)

    override fun mouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int): Boolean {
        if (active && visible && isValidClickButton(mouseButton) && clicked(mouseX, mouseY)) {
            playDownSound(Minecraft.getInstance().soundHandler)
            onCLick(mouseButton)
            return true
        }
        return false
    }

    override fun isValidClickButton(button: Int): Boolean {
        return button in validClicks
    }
}