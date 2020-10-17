package mnm.mods.tabbychat.util

import net.minecraft.util.text.TextFormatting

/**
 * Represents a color and provides an easy way to convert to and from html color
 * codes. A few default colors have been provided via the [Colors] object.
 */
typealias Color = Int

/**
 * The color's alpha value
 */
val Color.alpha get() = this shr 24 and 255

/**
 * The color's red value
 */
val Color.red get() = this shr 16 and 255

/**
 * The color's green value
 */
val Color.green get() = this shr 8 and 255

/**
 * The color's blue value
 */
val Color.blue get() = this and 255

/**
 * The color's string represenation.
 */
val Color.asString get() = "#" + this.asLong.toString(16).padStart(8, '0')

/**
 * The unsigned int value as a long. Will always be positive
 */
val Color.asLong get() = Integer.toUnsignedLong(this)

fun rgb(r: Int, g: Int, b: Int) = argb(255, r, g, b)

fun argb(a: Int, r: Int, g: Int, b: Int): Color {
    var c = 0
    c += ((a and 255) shl 24)
    c += ((r and 255) shl 16)
    c += ((g and 255) shl 8)
    c += ((b and 255))
    return c
}

fun color(c: Long): Color {
    return c.toInt()
}

fun color(s: String): Color? {
    // first, try to parse it as a hex.
    // if it's not a hex, it must be a name!
    return parseColorHex(s) ?: getVanillaColorByName(s)
}

private fun parseColorHex(s: String): Color? {
    val reg = Regex("""^#([a-f0-9]+)$""", RegexOption.IGNORE_CASE)
    val match = reg.find(s)
    if (match != null && s.length in intArrayOf(4, 7, 9)) {
        val hex = StringBuilder(match.groupValues[1])
        if (hex.length == 3) {
            hex.insert(2, hex[2])
            hex.insert(1, hex[1])
            hex.insert(0, hex[0])
        }
        if (hex.length == 6) {
            hex.insert(0, "ff")
        }
        val (alpha, red, green, blue) = hex.chunked(2) {
            it.toString().toInt(16)
        }
        return argb(alpha, red, green, blue)
    }
    return null
}

private fun getVanillaColorByName(s: String): Color? {
    return TextFormatting.getValueByName(s)
            // with full opacity
            ?.color?.with(a = 255)
}

/**
 * Color Function
 */
typealias CF = (Int) -> Int

fun Color.with(a: Int = alpha, r: Int = red, g: Int = green, b: Int = blue): Color {
    return with({ a }, { r }, { g }, { b })
}

fun Color.with(a: CF = { it }, r: CF = { it }, g: CF = { it }, b: CF = { it }): Color {
    return argb(a(alpha), r(red), g(green), b(blue))
}

object Colors {
    val BLACK = color("black")!!
    val DARK_BLUE = color("dark_blue")!!
    val DARK_GREEN = color("dark_green")!!
    val DARK_AQUA = color("dark_aqua")!!
    val DARK_RED = color("dark_red")!!
    val DARK_PURPLE = color("dark_purple")!!
    val GOLD = color("gold")!!
    val GRAY = color("gray")!!
    val DARK_GRAY = color("dark_gray")!!
    val BLUE = color("blue")!!
    val GREEN = color("green")!!
    val AQUA = color("aqua")!!
    val RED = color("red")!!
    val LIGHT_PURPLE = color("light_purple")!!
    val YELLOW = color("yellow")!!
    val WHITE = color("white")!!
}
