package mnm.mods.tabbychat.client.util

import mnm.mods.tabbychat.util.mc
import net.minecraft.util.math.MathHelper

/**
 * Modified version of ScaledResolution. Supports arbitrary dimensions
 */
class ScaledDimension(width: Int, height: Int) {

    val scaledWidth_double: Double
    val scaledHeight_double: Double
    var scaledWidth: Int
        private set
    var scaledHeight: Int
        private set
    var scaleFactor: Int
        private set

    init {
        this.scaledWidth = width
        this.scaledHeight = height
        this.scaleFactor = 1
        val unicode = mc.forceUnicodeFont
        var scale = mc.gameSettings.guiScale

        if (scale == 0) {
            scale = 1000
        }

        while (this.scaleFactor < scale && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor
        }

        if (unicode && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor
        }

        this.scaledWidth_double = this.scaledWidth.toDouble() / this.scaleFactor.toDouble()
        this.scaledHeight_double = this.scaledHeight.toDouble() / this.scaleFactor.toDouble()
        this.scaledWidth = MathHelper.ceil(this.scaledWidth_double)
        this.scaledHeight = MathHelper.ceil(this.scaledHeight_double)
    }
}
