package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout.Position
import mnm.mods.tabbychat.util.mc
import org.lwjgl.opengl.GL11

/**
 * TODO: Horizontal scrolling
 *
 * @author Matthew
 */
class GuiScrollingPanel : GuiPanel(BorderLayout()) {

    val contentPanel = GuiPanel().apply {
        location = Location(0, 0, 100000, 100000)
    }

    override var minimumSize: Dim
        get() = location.size
        set(value: Dim) {
            super.minimumSize = value
        }

    init {
        this.add(GuiPanel().apply {
            add(contentPanel)
        }, Position.CENTER)
        this.add(Scrollbar(), Position.EAST)
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {

        val rect = location
        this.contentPanel.location = contentPanel.location.copy().apply {
            xPos = rect.xPos
        }

        val height = mc.mainWindow.height.toDouble()
        val scale = mc.mainWindow.guiScaleFactor

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor((rect.xPos * scale).toInt(), (height - rect.yHeight * scale).toInt(),
                (rect.width * scale + 1).toInt(), (rect.height * scale + 1).toInt())

        super.render(mouseX, mouseY, parTicks)

        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        val rect = contentPanel.location.copy()
        val scr = (rect.yPos + scroll * 8).toInt()
        rect.yPos = scr

        val prect = contentPanel.parent!!.location
        val (_, height) = contentPanel.minimumSize
        if (rect.yPos + height < prect.height) {
            rect.yPos = prect.height - height
        }
        val parent = parent

        if (rect.yPos > parent!!.location.yPos) {
            rect.yPos = parent.location.yPos
        }

        contentPanel.location = rect
        return true
    }

    // TODO Make draggable
    private inner class Scrollbar : GuiComponent() {

        override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
            val loc = this@GuiScrollingPanel.location
            val scroll = contentPanel.location.yPos
            val min = loc.yPos
            val max = loc.yHeight
            var total = contentPanel.minimumSize.height
            if (total <= max) {
                return
            }
            total -= max
            fill(0, 20, 10, 10, -1)
            val size = Math.max(max / 2, 10)
            val perc = scroll.toFloat() / total.toFloat() * (size.toFloat() / max.toFloat())
            val pos = (-perc * max).toInt()

            fill(loc.xPos - 1, loc.yPos + pos, loc.xPos, loc.yPos + pos + size - 1, -1)
            super.render(mouseX, mouseY, parTicks)
        }
    }
}
