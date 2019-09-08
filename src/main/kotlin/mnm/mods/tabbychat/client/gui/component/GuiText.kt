package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.widget.TextFieldWidget
import kotlin.properties.Delegates

/**
 * A gui component that wraps [TextFieldWidget].
 *
 * @author Matthew
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
//            renderCaption(hint!!, mouseX + 1, mouseY - 5)
        }
    }
}
