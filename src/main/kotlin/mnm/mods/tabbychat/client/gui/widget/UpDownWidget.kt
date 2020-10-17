package mnm.mods.tabbychat.client.gui.widget

import mnm.mods.tabbychat.util.Colors
import mnm.mods.tabbychat.util.property
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.widget.Widget
import net.minecraft.client.gui.widget.button.Button
import java.text.NumberFormat
import kotlin.reflect.KMutableProperty0

/**
 * Input for numbers, also known as a slider. Use [DoubleUpDownWidget] for
 * doubles and [IntUpDownWidget] for integers.
 *
 * @param <T> The number type.
 */
sealed class AbstractUpDownWidget<T : Number>(
        value: KMutableProperty0<T>,
        var minValue: T, var maxValue: T, var interval: T,
        x: Int, y: Int, w: Int, h: Int)
    : Widget(x, y, w, h, "") {

    var value by property(value)

    var format: NumberFormat = NumberFormat.getNumberInstance()

    private val btnHeight = height / 2

    private var up = Button(x + width - btnHeight, y, btnHeight, btnHeight, "\u2191") {
        increment(1)
    }
    private var down = Button(x + width - btnHeight, y + height - btnHeight, btnHeight, btnHeight, "\u2193") {
        increment(-1)
    }

    override fun getMessage(): String {
        return value.toString()
    }

    override fun render(p_render_1_: Int, p_render_2_: Int, p_render_3_: Float) {
        super.render(p_render_1_, p_render_2_, p_render_3_)
        up.render(p_render_1_, p_render_2_, p_render_3_)
        down.render(p_render_1_, p_render_2_, p_render_3_)
    }

    override fun renderBg(p_renderBg_1_: Minecraft, p_renderBg_2_: Int, p_renderBg_3_: Int) {
        fill(x, y, x + width, y + height, Colors.BLACK)
    }

    /**
     * Increments the value by `n * interval`. Use a negative number to go down.
     *
     * Essentially runs `value = value + (n * interval)`
     *
     * @param n The amount to increment
     */
    abstract fun increment(n: Int)
}

/**
 * A Numeric Up Down for integers.
 */
class IntUpDownWidget(value: KMutableProperty0<Int>, x: Int, y: Int, w: Int, h: Int)
    : AbstractUpDownWidget<Int>(value, Integer.MIN_VALUE, Integer.MAX_VALUE, 1,
        x, y, w, h) {
    override fun increment(n: Int) {
        value = (value + n * interval).coerceIn(minValue, maxValue)
    }
}

/**
 * A Numeric Up Down for doubles.
 */
class DoubleUpDownWidget(value: KMutableProperty0<Double>, x: Int, y: Int, w: Int, h: Int)
    : AbstractUpDownWidget<Double>(value, Double.MIN_VALUE, Double.MAX_VALUE, 1.0,
        x, y, w, h) {
    override fun increment(n: Int) {
        value = (value + n * interval).coerceIn(minValue, maxValue)
    }
}

