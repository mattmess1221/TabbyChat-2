package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout.Position
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.mc
import org.lwjgl.opengl.GL11
import kotlin.math.max

/**
 * TODO: Horizontal scrolling
 *
 * @author Matthew
 */
class GuiScrollingPanel : GuiPanel() {

    val contentPanel = GuiPanel().apply {
        location = Location(0, 0, 100000, 100000)
    }

    override var minimumSize: Dim
        get() = location.size
        set(value) {
            super.minimumSize = value
        }

    init {
        layout = BorderLayout()
        this.add(GuiPanel(), Position.CENTER) {
            add(contentPanel)
        }
        this.add(Scrollbar(), Position.EAST)
    }

    override fun render(x: Int, y: Int, parTicks: Float) {

        val rect = location
        this.contentPanel.location = contentPanel.location.copy().apply {
            xPos = rect.xPos
            val height = contentPanel.minimumSize.height
            if (yPos - height <= rect.yPos) {
                yPos = rect.yPos
            }
            if (yPos > rect.yHeight) {
                yPos = rect.yHeight - height
            }
        }

        val height = mc.func_228018_at_().height.toDouble()
        val scale = mc.func_228018_at_().guiScaleFactor

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor((rect.xPos * scale).toInt(), (height - rect.yHeight * scale).toInt(),
                (rect.width * scale + 1).toInt(), (rect.height * scale + 1).toInt())

        super.render(x, y, parTicks)

        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        contentPanel.location = contentPanel.location.copy().apply {
            yPos = (yPos + scroll * 8).toInt()
        }
        return true
    }

    // TODO Make draggable
    private inner class Scrollbar : GuiComponent() {

        override fun render(x: Int, y: Int, parTicks: Float) {
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
            val size = max(max / 2, 10)
            val perc = scroll.toFloat() / total.toFloat() * (size.toFloat() / max.toFloat())
            val pos = (-perc * max).toInt()

            fill(loc.xPos - 1, loc.yPos + pos, loc.xPos, loc.yPos + pos + size - 1, -1)
            super.render(x, y, parTicks)
        }
    }
}
