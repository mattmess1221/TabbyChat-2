package mnm.mods.tabbychat.util;

import com.google.gson.annotations.SerializedName;

import java.util.Random;

/**
 * Represents a color and provides an easy way to convert to and from html color
 * codes. A few default colors have been provided.
 */
public class Color {

    public static final Color BLACK = of(0xff000000);
    public static final Color DARK_BLUE = of(0xff0000aa);
    public static final Color DARK_GREEN = of(0xff00aa00);
    public static final Color DARK_AQUA = of(0xff00aaaa);
    public static final Color DARK_RED = of(0xffaa0000);
    public static final Color DARK_PURPLE = of(0xffaa00aa);
    public static final Color GOLD = of(0xffffaa00);
    public static final Color GRAY = of(0xffaaaaaa);
    public static final Color DARK_GRAY = of(0xff555555);
    public static final Color BLUE = of(0xff5555ff);
    public static final Color GREEN = of(0xff55ff55);
    public static final Color AQUA = of(0xff55ffff);
    public static final Color RED = of(0xffff5555);
    public static final Color LIGHT_PURPLE = of(0xffff55ff);
    public static final Color YELLOW = of(0xffffff55);
    public static final Color WHITE = of(0xffffffff);

    private static Random random = new Random();

    @SerializedName("r")
    private final int red;
    @SerializedName("g")
    private final int green;
    @SerializedName("b")
    private final int blue;
    @SerializedName("a")
    private final int alpha;

    /**
     * Creates a color using an html color code.
     *
     * @param hexColor The html color code
     */
    private Color(int hexColor) {
        this.alpha = hexColor >> 24 & 255;
        this.red = hexColor >> 16 & 255;
        this.green = hexColor >> 8 & 255;
        this.blue = hexColor & 255;
    }

    /**
     * Creates a color using RGBA color format.
     *
     * @param red Red color
     * @param green Green color
     * @param blue Blue color
     * @param alpha Transparency
     */
    private Color(int red, int green, int blue, int alpha) {
        this.red = red % 256;
        this.green = green % 256;
        this.blue = blue % 256;
        this.alpha = alpha % 256;
    }

    /**
     * Gets the html hex color code of this color usable by Minecraft's
     * {@link net.minecraft.client.gui.Gui}.
     *
     * @return The html hex code.
     */
    public int getHex() {
        int alphaI = alpha << 24;
        int redI = red << 16;
        int greenI = green << 8;
        return alphaI + redI + greenI + blue;
    }

    /**
     * Gets the red value.
     *
     * @return The red value
     */
    public int getRed() {
        return red;
    }

    /**
     * Gets the green value.
     *
     * @return The green value
     */
    public int getGreen() {
        return green;
    }

    /**
     * Gets the blue value.
     *
     * @return The blue value
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Gets the alpha value.
     *
     * @return The alpha value
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * Convenience method for getting the html code for a RGBA color. Creates a
     * new object and calls {@link Color#getHex()}.
     *
     * @param red The red value
     * @param green The green value
     * @param blue The blue value
     * @param alpha The alpha value
     * @return The hexadecimal representation of this color.
     */
    public static int getColor(int red, int green, int blue, int alpha) {
        return of(red, green, blue, alpha).getHex();
    }

    /**
     * Generates a random color.
     *
     * @return A random color
     */
    public static Color random() {
        return of(random.nextInt());
    }

    public static Color of(int hexColor) {
        return new Color(hexColor);
    }

    public static Color of(int red, int green, int blue, int alpha) {
        return new Color(red, green, blue, alpha);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + alpha;
        result = prime * result + blue;
        result = prime * result + green;
        result = prime * result + red;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof Color))
            return false;
        Color other = (Color) obj;
        return alpha == other.alpha
                && blue == other.blue
                && green == other.green
                && red == other.red;
    }
}
