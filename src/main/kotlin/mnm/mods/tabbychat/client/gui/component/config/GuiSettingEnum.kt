package mnm.mods.tabbychat.client.gui.component.config

import com.google.common.collect.ImmutableList
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Translatable
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.mc

/**
 * A setting for set values, such as enums. Values should either have a
 * toString() method or provide a names array during construction.
 *
 * @param <T> The type
 * @author Matthew
 */
class GuiSettingEnum<T>(
        setting: Value<T>,
        values: Array<T>,
        private val namer: T.() -> Translatable = {
            this as? Translatable ?: { toString() } as Translatable
        }) : GuiSetting<T>() {

    companion object {
        fun <T : Translatable> of(setting: Value<T>, values: Array<T>) = GuiSettingEnum(setting, values) {
            this
        }
    }

    private val values: List<T> = ImmutableList.copyOf(values)

    private var text: String = ""
    override var value: T = setting.value
        set(value) {
            this.text = namer(value).translate()
            field = value
        }

    init {
        secondaryColor = Color.DARK_GRAY
    }

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        if (location.contains(x, y)) {
            val selected = this.values.indexOf(this.value)
            val max = this.values.size
            var mov = 0

            if (button == 0) {
                // Left click, go forward
                mov = 1
            } else if (button == 1) {
                // Right click, go backward
                mov = -1
            }

            if (mov != 0) {
                // find the index and increment or decrement
                var id = selected + mov
                id = if (id < 0) (max - id) % max else id % max

                value = values[id]
            }
            return true
        }
        return false
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        val loc = this.location
        fill(loc.xPos, loc.yPos, loc.xWidth, loc.yHeight, -0x1000000)
        val string = mc.fontRenderer.trimStringToWidth(text, loc.width)
        val xPos = loc.xCenter - mc.fontRenderer.getStringWidth(string) / 2
        val yPos = loc.yCenter - 4
        mc.fontRenderer.drawString(string, xPos.toFloat(), yPos.toFloat(), primaryColorProperty.hex)
        renderBorders(loc.xPos, loc.yPos, loc.xWidth, loc.yHeight)
    }
}
