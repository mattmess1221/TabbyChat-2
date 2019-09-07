package mnm.mods.tabbychat.client.gui.component.layout

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import org.apache.logging.log4j.LogManager

/**
 * A layout which places components along a grid. Add components with an int[]
 * array to define bounds. Array can be of length 2 or 4, the last 2 being
 * optional. If they are not included, they default to 1.
 *
 *
 * The format of the array is `{ xPos, yPos, width*, height* }`. <br></br>
 * * indicates optional
 *
 *
 * **Example Usage:**
 *
 * <pre>
 * GuiPanel panel = new GuiPanel(new GuiGridLayout(10, 10));
 *
 * // creates a label and places it at x:0 y:0 and size w:1 h:1.
 * GuiLabel label = new GuiLabel(&quot;Label&quot;);
 * panel.addComponent(label, new int[] { 0, 0 });
 *
 * // creates a button and places it at x:1 y:1 with size w:3, h:2.
 * GuiButton button = new GuiButton(&quot;button&quot;);
 * panel.addComponent(button, new int[] { 1, 1, 3, 2 });
 * </pre>
 *
 * @author Matthew
 */
class GuiGridLayout(private val cols: Int, private val rows: Int) : ILayout {

    private val grid = HashMap<ILocation, GuiComponent>()

    override val layoutSize: Dim
        get() = Dim(cols, rows)

    override fun addComponent(comp: GuiComponent, constraints: Any?) {
        if (constraints == null) {
            throw IllegalArgumentException("component requires constraints.")
        } else if (constraints !is IntArray) {
            throw IllegalArgumentException("Constraints must be an int array")
        }
        addComponent(comp, (constraints as IntArray?)!!)
    }

    private fun addComponent(comp: GuiComponent, constraints: IntArray) {
        if (constraints.size != 2 && constraints.size != 4) {
            throw IllegalArgumentException("Constraints must have either 2 or 4 elements.")
        }
        val x = constraints[0]
        val y = constraints[1]
        var w = 1
        var h = 1
        if (constraints.size == 4) {
            w = constraints[2]
            h = constraints[3]
        }
        val rect = Location(x, y, w, h)
        try {
            checkBoundsIfValid(rect)
            grid[rect.asImmutable()] = comp
        } catch (e: Exception) {
            LogManager.getLogger().catching(e)
        }

    }

    /**
     * Checks if the rectangle is valid. A rectangle is valid if x and y are
     * above 0 and x + width < cols and y + height < rows
     */
    private fun checkBoundsIfValid(b: ILocation) {
        if (b.xWidth - 1 > cols || b.yHeight - 1 > rows || b.xPos < 0 || b.yPos < 0) {
            throw IndexOutOfBoundsException("x:${b.xPos} y:${b.yPos} w:${b.width} h:${b.height} cols:$cols rows:$rows")
        }
        for ((r, c) in grid) {
            if (b.contains(r)) {
                throw IllegalArgumentException("Area $b already contains ${c.javaClass.name}")
            }
        }
    }

    override fun removeComponent(comp: GuiComponent) {
        var remove: ILocation? = null
        for ((key, value) in grid) {
            if (value === comp) {
                remove = key
                break
            }
        }
        grid.remove(remove)
    }

    override fun layoutComponents(parent: GuiPanel) {
        val loc = parent.location
        val colW = loc.width / cols
        val rowH = loc.height / rows
        for ((bounds, value) in grid) {
            val x = loc.xPos + bounds.xPos * colW
            val width = bounds.width * colW
            val y = loc.yPos + bounds.yPos * rowH
            val height = bounds.height * rowH
            value.location = Location(x, y, width, height)
        }
    }

}
