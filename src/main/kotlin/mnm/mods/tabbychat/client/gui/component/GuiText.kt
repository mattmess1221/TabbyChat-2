package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.widget.TextFieldWidget

/**
 * A gui component that wraps [TextFieldWidget].
 *
 * @author Matthew
 */
open class GuiText(
        val textField: TextFieldWidget = TextFieldWidget(mc.fontRenderer, 0, 0, 1, 1, "")
) : GuiWrappedEventListener<TextFieldWidget>(textField), IGuiInput<String> {
    var hint: String? = null

    override var location: ILocation
        get() = super.location
        set(bounds) {
            updateTextbox(bounds)
            super.location = bounds
        }

    override var primaryColor: Color?
        get() = super.primaryColor
        set(foreColor) {
            foreColor?.run { textField.setTextColor(hex) }
            super.primaryColor = foreColor
        }

    override var value: String
        get() = textField.text
        set(value) {
            textField.text = value
        }

    init {
        // This text field must not be calibrated for someone of your...
        // generous..ness
        // I'll add a few 0s to the maximum length...
        this.textField.maxStringLength = 10000

        // you look great, by the way.

    }

    private fun updateTextbox(loc: ILocation) {
        this.textField.x = loc.xPos
        this.textField.y = loc.yPos
        this.textField.width = loc.width
        this.textField.height = loc.height
    }

    override fun tick() {
        textField.tick()
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        textField.render(mouseX, mouseY, parTicks)

        super.render(mouseX, mouseY, parTicks)
        if (textField.isFocused && !hint.isNullOrEmpty()) {
            // draw the hint above.
//            renderCaption(hint!!, mouseX + 1, mouseY - 5)
        }
    }
}
