package mnm.mods.tabbychat.client.gui.component.layout

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.Dim
import kotlin.math.max

/**
 * Displays components top to bottom. Like [FlowLayout], but vertical.
 *
 * @author Matthew
 */
class VerticalLayout : ILayout {

    private val list = ArrayList<GuiComponent>()

    override val layoutSize: Dim
        get() {
            var width = 0
            var height = 0
            for (comp in list) {
                width = max(width, comp.location.width)
                height += comp.minimumSize.height
            }
            return Dim(width, height)
        }

    override fun addComponent(comp: GuiComponent, constraints: Any?) {
        if (constraints != null) {
            if (constraints is Int) {
                list.add((constraints as Int?)!!, comp)
            } else {
                throw IllegalArgumentException("Illegal constraint of type: ${constraints.javaClass.name}. Only int accepted.")
            }
        } else {
            list.add(comp)
        }
    }

    override fun removeComponent(comp: GuiComponent) {
        list.remove(comp)
    }

    override fun layoutComponents(parent: GuiPanel) {
        val par = parent.location
        var y = par.yPos
        for (comp in list) {
            val loc = comp.location.copy()
            loc.xPos = par.xPos
            loc.yPos = y
            comp.location = loc
            y += comp.minimumSize.height
        }
    }
}
