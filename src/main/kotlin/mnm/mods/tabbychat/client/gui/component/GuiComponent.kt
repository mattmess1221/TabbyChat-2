package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.*
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.IRenderable
import net.minecraft.client.gui.screen.Screen
import net.minecraftforge.fml.client.config.GuiUtils

/**
 * The base class for all gui components.
 *
 * @author Matthew
 */
abstract class GuiComponent : AbstractGui(), IRenderable, IGuiEventListener {

    open var parent: GuiPanel? = null

    /**
     * The location of this component. In order to maintain encapsulation, it
     * is wrapped as an immutable.
     */
    open var location: ILocation = Location()
        set(location) {
            field = location.asImmutable()
        }

    open var minimumSize = Dim(0, 0)

    open var active: Boolean = true

    open var visible: Boolean = true
        set(visible) {
            if (!visible) {
                this.onClosed()
            }
            field = visible
        }

    open var focused = false
    open var hovered = false

    open var secondaryColor: Color? = null
    open var primaryColor: Color? = null

    val primaryColorProperty: Color
        get() = getProperty { it.primaryColor } ?: Color.WHITE

    val secondaryColorProperty: Color
        get() = getProperty { it.secondaryColor } ?: Color.NONE


    override fun render(x: Int, y: Int, parTicks: Float) {
        hovered = location.contains(x, y)
    }

    protected fun renderBorders(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        this.vLine(x1 - 1, y1 - 1, y2 + 1, color) // left
        this.hLine(x1 - 1, x2, y1 - 1, color) // top
        this.vLine(x2, y1 - 1, y2 + 1, color) // right
        this.hLine(x1, x2 - 1, y2, color) // bottom
    }

    /**
     * Draws borders around the provided points. Uses a brightened background
     * color with 0xaa transparency.
     *
     * @param x1 left point
     * @param y1 upper point
     * @param x2 right point
     * @param y2 lower point
     */
    protected fun renderBorders(x1: Int, y1: Int, x2: Int, y2: Int) {
        var color = secondaryColorProperty
        var r = color.red
        var g = color.green
        var b = color.blue
        val amt = .75
        r += lum(r, amt)
        g += lum(g, amt)
        b += lum(b, amt)
        color = Color(r, g, b, 0xaa)
        renderBorders(x1, y1, x2, y2, color.hex)
    }

    private fun lum(o: Int, amt: Double): Int {
        return ((255 - o) * amt).toInt()
    }

    open fun onClick(x: Double, y: Double) = Unit
    open fun onRelease(x: Double, y: Double) = Unit
    open fun onDrag(x: Double, y: Double, dx: Double, dy: Double) = Unit

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        if (active && visible && isValidButton(button) && this.clicked(x, y)) {
            this.onClick(x, y)
            return true
        }
        return false
    }

    override fun mouseReleased(x: Double, y: Double, button: Int): Boolean {
        if (isValidButton(button)) {
            onRelease(x, y)
            return true
        }
        return false
    }

    override fun mouseDragged(x: Double, y: Double, button: Int, dx: Double, dy: Double): Boolean {
        if (isValidButton(button)) {
            onDrag(x, y, dx, dy)
            return true
        }
        return false
    }

    open fun isValidButton(button: Int) = button == 0

    override fun changeFocus(focus: Boolean): Boolean {
        if (active && visible) {
            focused = !focused
            onFocusedChanged(focused)
            return this.focused
        }
        return false
    }

    open fun onFocusedChanged(focus: Boolean) = Unit

    override fun isMouseOver(x: Double, y: Double): Boolean {
        return this.active && this.visible && location.contains(x, y)
    }

    /**
     * Updates the component. Called when it is called on the [Screen].
     */
    open fun tick() {}

    /**
     * Called when the screen is closed.
     */
    open fun onClosed() {}

    fun clicked(x: Double, y: Double): Boolean {
        return isMouseOver(x, y)
    }
    
    internal fun <T> getProperty(prop: (GuiComponent) -> T?): T? {
        return prop(this) ?: parent?.getProperty(prop)
    }

    protected fun drawModalCorners(modal: TexturedModal) {
        val location = location
        val x = location.xPos
        val y = location.yPos
        val u = modal.xPos
        val v = modal.yPos
        val w = location.width + 1
        val h = location.height + 1
        val uw = modal.width
        val uh = modal.height

        GuiUtils.drawContinuousTexturedBox(modal.resourceLocation, x, y, u, v, w, h, uw, uh, 2, blitOffset.toFloat())
    }
}
