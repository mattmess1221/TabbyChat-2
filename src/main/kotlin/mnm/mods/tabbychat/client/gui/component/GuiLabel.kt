package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.text.FancyFontRenderer
import net.minecraft.client.Minecraft
import net.minecraft.util.text.ITextComponent

/**
 * Gui component label used to show text on the screen.
 *
 * @author Matthew
 */
open class GuiLabel(var text: ITextComponent? = null) : GuiComponent() {

    private val fr: FancyFontRenderer = FancyFontRenderer(Minecraft.getInstance().fontRenderer)

    var angle: Float = 0.toFloat()

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (text == null)
            return
        GlStateManager.pushMatrix()

        GlStateManager.rotatef(angle, 0f, 0f, angle)
        if (angle < 180) {
            GlStateManager.translated(-angle / 1.5, (-angle / 4).toDouble(), 0.0)
        } else {
            GlStateManager.translated((-angle / 15).toDouble(), (angle / 40).toDouble(), 0.0)
        }

        val loc = location
        fr.drawChat(text!!, (loc.xPos + 3).toFloat(), (loc.yPos + 3).toFloat(), primaryColorProperty.hex, true)

        GlStateManager.popMatrix()
    }

}
