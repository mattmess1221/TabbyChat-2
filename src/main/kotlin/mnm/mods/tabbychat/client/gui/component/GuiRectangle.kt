package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.*
import mnm.mods.tabbychat.util.ILocation
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
        GlStateManager.enableBlend()
        mc.getTextureManager().bindTexture(TRANSPARENCY)
        val loc = location
        blit(loc.xPos, loc.yPos, 0f, 0f, loc.width, loc.height, 5, 5)
        fill(loc.xPos, loc.yPos, loc.xWidth, loc.yHeight, primaryColorProperty.hex)
        GlStateManager.disableBlend()
    }

    companion object {
        private val TRANSPARENCY = ResourceLocation(MODID, "textures/transparency.png")
    }
}
