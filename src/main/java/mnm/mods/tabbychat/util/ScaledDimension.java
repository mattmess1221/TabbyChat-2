package mnm.mods.tabbychat.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

/**
 * Modified version of ScaledResolution. Supports arbitrary dimensions
 * 
 * @author Matthew
 */
public class ScaledDimension {

    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;

    public ScaledDimension(int width, int height) {
        this.scaledWidth = width;
        this.scaledHeight = height;
        this.scaleFactor = 1;
        Minecraft mc = Minecraft.getMinecraft();
        boolean unicode = mc.isUnicode();
        int scale = mc.gameSettings.guiScale;

        if (scale == 0) {
            scale = 1000;
        }

        while (this.scaleFactor < scale && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        if (unicode && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }

        this.scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        this.scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
    }

    public int getScaledWidth() {
        return this.scaledWidth;
    }

    public int getScaledHeight() {
        return this.scaledHeight;
    }

    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }

    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }

    public int getScaleFactor() {
        return this.scaleFactor;
    }
}
