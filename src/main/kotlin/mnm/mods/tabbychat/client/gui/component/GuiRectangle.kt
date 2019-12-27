package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.util.mc
import net.minecraft.util.ResourceLocation

/**
 * Represents a colored area. Transparency is represented using a transparency
 * grid.
 *
 * @author Matthew
 */
open class GuiRectangle : GuiComponent() {

    override fun render(x: Int, y: Int, parTicks: Float) {
        RenderSystem.enableBlend()
        mc.getTextureManager().bindTexture(TRANSPARENCY)
        val loc = location
        blit(loc.xPos, loc.yPos, 0f, 0f, loc.width, loc.height, 5, 5)
        fill(loc.xPos, loc.yPos, loc.xWidth, loc.yHeight, primaryColorProperty.hex)
        RenderSystem.disableBlend()
    }

    companion object {
        private val TRANSPARENCY = ResourceLocation(MODID, "textures/transparency.png")
    }
}
