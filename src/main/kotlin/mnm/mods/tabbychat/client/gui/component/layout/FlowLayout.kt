package mnm.mods.tabbychat.client.gui.component.layout

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.Location
import kotlin.math.max

/**
 * A layout that puts items side-by-side and left-to-right.
 *
 * @author Matthew
 */
class FlowLayout : ILayout {

    private val components = ArrayList<GuiComponent>()

    override val layoutSize: Dim
        get() {
            var width = 0
            var height = 0

            for (comp in components) {
                val loc = comp.location
                width += loc.width
                height = max(height, loc.height)
            }
            return Dim(width, height)
        }

    override fun addComponent(comp: GuiComponent, constraints: Any?) {
        components.add(comp)
    }

    override fun removeComponent(comp: GuiComponent) {
        components.remove(comp)
    }

    override fun layoutComponents(parent: GuiPanel) {

        val loc = parent.location
        var xPos = loc.xPos
        var yPos = loc.yPos
        var maxH = 0
        for (comp in components) {
            val (width, height) = comp.minimumSize
            if (xPos + width > loc.xWidth) {
                // wrapping
                xPos = loc.xPos
                yPos += maxH
                maxH = 0
            }
            comp.location = Location(xPos, yPos, width, height)

            maxH = max(maxH, height)
            xPos += width
        }

    }

}
