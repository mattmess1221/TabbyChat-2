package mnm.mods.tabbychat.client.gui.component.layout

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.Location
import java.util.*

/**
 * A recreation of Border Layout.
 *
 * @author Matthew Matthew
 * @see java.awt.BorderLayout
 */
class BorderLayout : ILayout {

    private val components = EnumMap<Position, GuiComponent>(Position::class.java)

    override val layoutSize: Dim
        get() {
            var width = 0
            var height = 0
            for ((key, value) in components) {
                val (width1, height1) = value.minimumSize
                when (key!!) {
                    Position.CENTER -> {
                        width += width1
                        height += height1
                    }
                    Position.EAST, Position.WEST -> width += width1
                    Position.NORTH, Position.SOUTH -> height += height1
                }
            }
            return Dim(width, height)
        }

    override fun addComponent(comp: GuiComponent, constraints: Any?) {
        if (constraints == null || constraints is Position) {
            addComponent(constraints as Position?, comp)
        } else {
            throw IllegalArgumentException(
                    "cannot add to layout: constraint must be a Position enum")
        }
    }

    @Synchronized
    private fun addComponent(constraint: Position?, comp: GuiComponent) {
        components[constraint ?: Position.CENTER] = comp
    }

    @Synchronized
    override fun removeComponent(comp: GuiComponent) {
        components.values.remove(comp)
    }

    override fun layoutComponents(parent: GuiPanel) {
        val pbounds = parent.location
        val center = components[Position.CENTER]
        val north = components[Position.NORTH]
        val south = components[Position.SOUTH]
        val west = components[Position.WEST]
        val east = components[Position.EAST]

        if (north != null) {
            north.location = Location(pbounds.xPos, pbounds.yPos, pbounds.width, north.minimumSize.height)
        }

        if (west != null) {
            val bounds = pbounds.copy()
            bounds.width = west.minimumSize.width

            if (north != null) {
                bounds.yPos = north.location.yHeight
            }

            if (south == null) {
                if (north == null) {
                    bounds.height = pbounds.height
                } else {
                    bounds.height = pbounds.height - north.location.height
                }
            } else {
                if (north == null) {
                    bounds.height = pbounds.height - south.location.height
                } else {
                    bounds.height = pbounds.height - south.location.height - north.location.height
                }
            }
            west.location = bounds
        }

        if (center != null) {
            val bounds = pbounds.copy()

            if (north != null) {
                bounds.yPos = north.location.yHeight + 1
            }

            if (west != null) {
                bounds.xPos = west.location.xWidth
            }

            if (east == null) {
                if (west == null) {
                    bounds.width = pbounds.width
                } else {
                    bounds.width = pbounds.width - west.location.width
                }
            } else {
                if (west == null) {
                    bounds.width = pbounds.width - east.location.width
                } else {
                    bounds.width = pbounds.width - east.location.width - west.location.width
                }
            }

            if (south == null) {
                if (north == null) {
                    bounds.height = pbounds.height
                } else {
                    bounds.height = pbounds.height - north.location.height
                }
            } else {
                if (north == null) {
                    bounds.height = pbounds.height - south.location.height - 1
                } else {
                    bounds.height = pbounds.height - south.location.height - north.location.height - 2
                }
            }
            center.location = bounds
        }

        if (east != null) {
            val bounds = pbounds.copy()

            bounds.xPos = pbounds.xWidth - east.minimumSize.width
            bounds.width = east.minimumSize.width
            if (north != null) {
                bounds.yPos = north.location.yHeight
            }
            if (south == null) {
                if (north == null) {
                    bounds.height = pbounds.height
                } else {
                    bounds.height = pbounds.height - north.minimumSize.height
                }
            } else {
                if (north == null) {
                    bounds.height = pbounds.height - south.minimumSize.height
                } else {
                    bounds.height = pbounds.height - south.minimumSize.height - north.minimumSize.height
                }
            }
            east.location = bounds
        }

        if (south != null) {

            val x = pbounds.xPos
            val y = pbounds.yHeight - south.location.height
            val width = pbounds.width
            val height = south.minimumSize.height

            south.location = Location(x, y, width, height)
        }
    }

    enum class Position {
        NORTH,
        SOUTH,
        EAST,
        WEST,
        CENTER
    }
}
