package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.client.gui.component.GuiNumericUpDown.DoubleUpDown
import mnm.mods.tabbychat.client.gui.component.GuiNumericUpDown.IntUpDown
import mnm.mods.tabbychat.client.gui.component.layout.AbsoluteLayout
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import net.minecraft.util.text.StringTextComponent
import java.text.NumberFormat

/**
 * Input for numbers, also known as a slider. Use [DoubleUpDown] for
 * doubles and [IntUpDown] for integers.
 *
 * @param <T> The number type.
 * @author Matthew
</T> */
abstract class GuiNumericUpDown<T : Number> private constructor() : GuiPanel(), IGuiInput<T> {

    var min = java.lang.Double.MIN_VALUE
    var max = java.lang.Double.MAX_VALUE
    var interval = 1.0

    var double: Double = 0.0
        set(value) {
            field = value.coerceIn(min, max)
        }

    var format = NumberFormat.getNumberInstance()

    init {
        layout = BorderLayout()

        run {
            val text = GuiPanel(AbsoluteLayout())
            val rect = object : GuiRectangle() {
                override var location: ILocation
                    get() = parent?.location ?: super.location
                    set(value) {
                        super.location = value
                    }
            }
            rect.primaryColor = Color.BLACK
            text.add(rect)

            val label = GuiLabel(StringTextComponent(format.format(value)))
            text.add(label, Location(5, 1, 0, 0))

            add(text, BorderLayout.Position.CENTER)
        }
        run {
            val pnlButtons = GuiPanel(AbsoluteLayout())
            val up = UpDown("\u2191", 1) // up arrow
            val down = UpDown("\u2193", -1) // down arrow
            pnlButtons.add(up, Location(0, 0, 5, 5))
            pnlButtons.add(down, Location(0, 5, 5, 5))

            add(pnlButtons, BorderLayout.Position.EAST)
        }
    }

    /**
     * Increments the value by `n`. Use a negative number to go down.
     *
     *
     * Essentially runs `value = value + (n * interval)`
     *
     * @param n The amount to increment
     */
    fun increment(n: Int) {
        double += n * interval
    }

    private inner class UpDown(override val text: String, private val direction: Int) : AbstractGuiButton() {

        init {
            secondaryColor = Color.DARK_GRAY
        }

        override fun onPress() {
            increment(direction)
        }
    }

    /**
     * A Numeric Up Down for integers.
     *
     * @author Matthew
     */
    class IntUpDown : GuiNumericUpDown<Int>() {
        override var value: Int
            get() = double.toInt()
            set(i) {
                double = i.toDouble()
            }
    }

    /**
     * A Numeric Up Down for doubles.
     *
     * @author Matthew
     */
    class DoubleUpDown : GuiNumericUpDown<Double>() {
        override var value: Double
            get() = double
            set(d) {
                double = d
            }
    }
}
