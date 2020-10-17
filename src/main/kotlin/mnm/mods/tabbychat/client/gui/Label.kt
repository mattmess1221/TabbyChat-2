package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.util.Translatable
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.IRenderable
import net.minecraft.util.text.ITextComponent

/**
 * Gui component label used to show text on the screen.
 */
class Label(private val font: FontRenderer,
            var text: ITextComponent,
            var xPos: Int,
            var yPos: Int,
            var color: Int = -1,
            var shadow: Boolean = true) : IRenderable {

    constructor(font: FontRenderer, text: Translatable, xPos: Int, yPos: Int, color: Int = -1, shadow: Boolean = true)
            : this(font, text.toComponent(), xPos, yPos, color, shadow)

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (shadow) {
            font.drawStringWithShadow(text.formattedText, xPos.toFloat(), yPos.toFloat(), color)
        } else {
            font.drawString(text.formattedText, xPos.toFloat(), yPos.toFloat(), color)
        }
    }

}
