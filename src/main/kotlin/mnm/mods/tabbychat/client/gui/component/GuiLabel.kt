package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.util.mc
import mnm.mods.tabbychat.util.text.FancyFontRenderer
import net.minecraft.util.text.ITextComponent

/**
 * Gui component label used to show text on the screen.
 *
 * @author Matthew
 */
open class GuiLabel(var text: ITextComponent? = null) : GuiComponent() {

    private val fr: FancyFontRenderer = FancyFontRenderer(mc.fontRenderer)

    var angle: Float = 0.toFloat()

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (text == null)
            return
        RenderSystem.pushMatrix()

        RenderSystem.rotatef(angle, 0f, 0f, angle)
        if (angle < 180) {
            RenderSystem.translated(-angle / 1.5, (-angle / 4).toDouble(), 0.0)
        } else {
            RenderSystem.translated((-angle / 15).toDouble(), (angle / 40).toDouble(), 0.0)
        }

        val loc = location
        fr.drawChat(text!!, (loc.xPos + 3).toFloat(), (loc.yPos + 3).toFloat(), primaryColorProperty.hex, true)

        RenderSystem.popMatrix()
    }

}
