package mnm.mods.tabbychat.util

import com.google.gson.annotations.SerializedName

import java.util.Random

/**
 * Represents a color and provides an easy way to convert to and from html color
 * codes. A few default colors have been provided.
 */
data class Color constructor(
        @SerializedName("r") val red: Int,
        @SerializedName("g") val green: Int,
        @SerializedName("b") val blue: Int,
        @SerializedName("a") val alpha: Int
){

    /**
     * Gets the html hex color code of this color usable by Minecraft's
     * [net.minecraft.client.gui.AbstractGui].
     *
     * @return The html hex code.
     */
    val hex: Int
        get() {
            val alphaI = alpha shl 24
            val redI = red shl 16
            val greenI = green shl 8
            return alphaI + redI + greenI + blue
        }

    /**
     * Creates a color using an html color code.
     *
     * @param hexColor The html color code
     */
    constructor(hexColor: Int) : this(
            red = hexColor shr 16 and 255,
            green = hexColor shr 8 and 255,
            blue = hexColor and 255,
            alpha = hexColor shr 24 and 255)

    companion object {
        val NONE = Color(0)
        val BLACK = Color(-0x1000000)
        val DARK_BLUE = Color(-0xffff56)
        val DARK_GREEN = Color(-0xff5600)
        val DARK_AQUA = Color(-0xff5556)
        val DARK_RED = Color(-0x560000)
        val DARK_PURPLE = Color(-0x55ff56)
        val GOLD = Color(-0x5600)
        val GRAY = Color(-0x555556)
        val DARK_GRAY = Color(-0xaaaaab)
        val BLUE = Color(-0xaaaa01)
        val GREEN = Color(-0xaa00ab)
        val AQUA = Color(-0xaa0001)
        val RED = Color(-0xaaab)
        val LIGHT_PURPLE = Color(-0xaa01)
        val YELLOW = Color(-0xab)
        val WHITE = Color(-0x1f)

        private val random = Random()

        /**
         * Convenience method for getting the html code for a RGBA color. Creates a
         * new object and calls [Color.hex].
         *
         * @param red The red value
         * @param green The green value
         * @param blue The blue value
         * @param alpha The alpha value
         * @return The hexadecimal representation of this color.
         */
        fun getHex(red: Int, green: Int, blue: Int, alpha: Int): Int {
            return Color(red, green, blue, alpha).hex
        }

        /**
         * Generates a random color.
         *
         * @return A random color
         */
        fun random(): Color {
            return Color(random.nextInt())
        }

    }
}
