package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.util.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.Widget
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.fml.client.config.GuiUtils
import org.apache.commons.lang3.StringEscapeUtils
import kotlin.math.max

/**
 * The base class for all gui components.
 *
 * @author Matthew
 */
abstract class GuiComponent : Widget(0, 0, "") {

    open var secondaryColor: Color? = null
    open var primaryColor: Color? = null
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
    open var caption: ITextComponent? = null

    val primaryColorProperty: Color
        get() = getProperty { it.primaryColor } ?: Color.WHITE

    val secondaryColorProperty: Color
        get() = getProperty { it.secondaryColor } ?: Color.NONE

    /**
     * Disabled components will not handle mouse or keyboard events.
     */
    open var isEnabled: Boolean
        get() = active
        set(enabled) {
            this.active = enabled
        }

    /**
     * The component's visibility. Non-visible components are not rendered.
     */
    open var isVisible: Boolean
        get() = visible
        set(visible) {
            if (!visible) {
                this.onClosed()
            }
            this.visible = visible
        }

    /**
     * Draws this component on screen.
     *
     * @param mouseX   The mouse x
     * @param mouseY   The mouse y
     * @param parTicks
     */
    override fun renderButton(mouseX: Int, mouseY: Int, parTicks: Float) {}

    open fun renderCaption(x: Int, y: Int) {
        caption?.formattedText
                ?.takeIf { it.isNotEmpty() && location.contains(x, y) }
                ?.also {
                    renderCaption(it, x, y)
                }
    }

    protected fun renderCaption(caption: String, x: Int, y: Int) {
        var caption = caption
        var x = x
        var y = y
        caption = StringEscapeUtils.unescapeJava(caption)
        val list = caption.lines().dropLastWhile { it.isEmpty() }

        var w = 0
        // find the largest width
        for (s in list) {
            w = max(w, mc.fontRenderer.getStringWidth(s))
        }
        y -= mc.fontRenderer.FONT_HEIGHT * list.size

        val point = location.point
        val sw = mc.mainWindow.scaledWidth
        var w2 = w
        val x2 = x
        while (x2 - 8 + point.x + w2 + 20 > sw) {
            x--
            w2--
        }
        x += location.xPos
        y += location.yPos
        // put it on top
        GlStateManager.pushMatrix()
        fill(x - 2, y - 2, x + w + 2, y + mc.fontRenderer.FONT_HEIGHT * list.size + 1, -0x33cccccd)
        renderBorders(x - 2, y - 2, x + w + 2, y + mc.fontRenderer.FONT_HEIGHT * list.size + 1, -0x33555556)
        for (s in list) {
            mc.fontRenderer.drawStringWithShadow(s, x.toFloat(), y.toFloat(), this.primaryColorProperty.hex)
            y += mc.fontRenderer.FONT_HEIGHT
        }
        GlStateManager.popMatrix()
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

    /**
     * Updates the component. Called when it is called on the [Screen].
     */
    open fun tick() {}

    /**
     * Called when the screen is closed.
     */
    open fun onClosed() {}

    override fun clicked(x: Double, y: Double): Boolean {
        return isMouseOver(x, y)
    }

    override fun isMouseOver(x: Double, y: Double): Boolean {
        return this.isEnabled && this.isVisible && location.contains(x, y)
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
