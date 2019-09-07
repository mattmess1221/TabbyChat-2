package mnm.mods.tabbychat.client.gui.component.layout

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import java.lang.Math.max

import java.util.IdentityHashMap

class AbsoluteLayout : ILayout {

    private val components = IdentityHashMap<GuiComponent, ILocation>()

    override val layoutSize: Dim
        get() {
            var width = 0
            var height = 0
            for (comp in components.keys) {
                width = max(width, comp.location.width)
                height = max(height, comp.location.height)
            }
            return Dim(width, height)
        }

    override fun addComponent(comp: GuiComponent, constraints: Any?) {
        if (constraints != null && constraints !is ILocation) {
            throw IllegalArgumentException("Nonnull constraint must be ILocation")
        }
        val loc: ILocation = constraints as? ILocation ?: Location()
        components[comp] = loc.asImmutable()
    }

    override fun removeComponent(comp: GuiComponent) {
        components.remove(comp)
    }

    override fun layoutComponents(parent: GuiPanel) {
        val ploc = parent.location
        for ((comp, value) in components) {
            val loc = value.copy()
            comp.location = loc.move(ploc.xPos, ploc.yPos)
        }
    }
}
