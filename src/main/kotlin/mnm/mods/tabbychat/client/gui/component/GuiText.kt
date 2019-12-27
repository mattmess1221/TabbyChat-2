package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.widget.TextFieldWidget
import org.apache.commons.lang3.StringEscapeUtils
import kotlin.properties.Delegates

/**
 * A gui component that wraps [TextFieldWidget].
 */
open class GuiText(
        textField: TextFieldWidget = TextFieldWidget(mc.fontRenderer, 0, 0, 1, 1, "")
) : GuiWrappedEventListener<TextFieldWidget>(textField), IGuiInput<String> {
    var hint: String? = null

    override var location by Delegates.observable(super.location) { _, _, n ->
        delegate.x = n.xPos
        delegate.y = n.yPos
        delegate.width = n.width
        delegate.height = n.height
    }

    override var primaryColor by Delegates.observable(super.primaryColor) { _, _, value ->
        delegate.setTextColor(value?.hex ?: -1)
    }

    override var value: String
        get() = delegate.text
        set(value) {
            delegate.text = value
            delegate.cursorPosition = 0
        }

    init {
        // This text field must not be calibrated for someone of your...
        // generous..ness
        // I'll add a few 0s to the maximum length...
        this.delegate.maxStringLength = 10000

        // you look great, by the way.

    }

    override fun tick() {
        delegate.tick()
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        delegate.render(x, y, parTicks)

        super.render(x, y, parTicks)
        if (delegate.isFocused && !hint.isNullOrEmpty()) {
            // draw the hint above.
            val loc = location
            renderHint(StringEscapeUtils.unescapeJava(hint), loc.xPos + 1, loc.yPos - 5)
        }
    }

    private fun renderHint(hint: String, xPos: Int, yPos: Int) {
        val list = hint.split("\n")

        // find the largest width
        val w = list.map { mc.fontRenderer.getStringWidth(it) }.max() ?: 0
        val h = mc.fontRenderer.FONT_HEIGHT * list.size
        val x = xPos
        var y = yPos - mc.fontRenderer.FONT_HEIGHT * list.size

        // put it on top
        RenderSystem.pushMatrix()
        fill(x - 2, y - 2, x + w + 2, y + h + 1, 0xcc333333.toInt())
        renderBorders(x - 2, y - 2, x + w + 2, y + h + 1, 0xccaaaaaa.toInt())

        list.forEach {
            mc.fontRenderer.drawStringWithShadow(it, x.toFloat(), y.toFloat(), -1)
            y += mc.fontRenderer.FONT_HEIGHT
        }

        RenderSystem.popMatrix()
    }
}
