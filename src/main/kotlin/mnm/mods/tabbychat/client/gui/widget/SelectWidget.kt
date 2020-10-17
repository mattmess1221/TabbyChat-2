package mnm.mods.tabbychat.client.gui.widget

import com.google.common.collect.ImmutableList
import mnm.mods.tabbychat.util.property
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import org.lwjgl.glfw.GLFW
import kotlin.reflect.KMutableProperty0

class EnumValues<T>(val values: Array<T>, val names: (T) -> ITextComponent = { StringTextComponent(it.toString()) })

/**
 * A setting for predefined values, such as enums. Values should either have a
 * toString() method or provide a names array during construction.
 *
 * TODO: The constructor arguments are clunky.
 *
 * @param <T> The type
 */
class SelectWidget<T : Any>(
        setting: KMutableProperty0<T>,
        private val enums: EnumValues<T>,
        xPos: Int,
        yPos: Int,
        width: Int,
        height: Int,
        title: String
) : AbstractMultiClickButton(xPos, yPos, width, height, title) {

    var value by property(setting)

    private val values: List<T> = ImmutableList.copyOf(enums.values)
    private val text get() = enums.names(this.value)

    override val validClicks = setOf(
            GLFW.GLFW_MOUSE_BUTTON_LEFT,
            GLFW.GLFW_MOUSE_BUTTON_RIGHT
    )

    init {
        updateButtonText()
    }

    override fun onCLick(button: Int) {
        when (button) {
            GLFW.GLFW_MOUSE_BUTTON_LEFT -> onPress(1)
            GLFW.GLFW_MOUSE_BUTTON_RIGHT -> onPress(-1)
        }
    }

    private fun updateButtonText() {
        message = text.formattedText
    }

    private fun onPress(mov: Int) {
        val selected = this.values.indexOf(this.value)
        val max = this.values.size

        if (mov != 0) {
            // find the index and increment or decrement
            var id = selected + mov
            id = if (id < 0) (max - id) % max else id % max

            value = values[id]
            updateButtonText()
        }
    }
}
