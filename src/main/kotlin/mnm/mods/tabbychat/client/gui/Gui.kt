package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.util.TexturedModal
import net.minecraft.client.gui.AbstractGui
import net.minecraftforge.fml.client.gui.GuiUtils

object AbstractGuiProxy : AbstractGui() {

    inline fun offset(offset: Int, action: AbstractGuiProxy.() -> Unit) {
        val originalOffset = blitOffset
        blitOffset = offset
        try {
            AbstractGuiProxy.action()
        } finally {
            blitOffset = originalOffset
        }
    }

    fun renderBorders(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        vLine(x1 - 1, y1 - 1, y2 + 1, color) // left
        this.hLine(x1 - 1, x2, y1 - 1, color) // top
        this.vLine(x2, y1 - 1, y2 + 1, color) // right
        this.hLine(x1, x2 - 1, y2, color) // bottom
    }
}

fun AbstractGui.renderBorders(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
    AbstractGuiProxy.offset(this.blitOffset) {
        renderBorders(x1, y1, x2, y2, color)
    }

    setOf<Any>() + setOf()
}

fun AbstractGui.drawModalCorners(x: Int, y: Int, w: Int, h: Int, modal: TexturedModal) {
    val u = modal.xPos
    val v = modal.yPos
    val uw = modal.width
    val uh = modal.height

    GuiUtils.drawContinuousTexturedBox(modal.resourceLocation, x, y, u, v, w, h, uw, uh, 2, blitOffset.toFloat())
}
