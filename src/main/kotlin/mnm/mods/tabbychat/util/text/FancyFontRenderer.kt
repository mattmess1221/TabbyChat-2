package mnm.mods.tabbychat.util.text

import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FontRenderer
import net.minecraft.util.text.ITextComponent

class FancyFontRenderer(private val fontRenderer: FontRenderer) : AbstractGui() {

    fun drawChat(chat: ITextComponent, x: Float, y: Float) {
        this.drawChat(chat, x, y, true)
    }

    fun drawChat(chat: ITextComponent, x: Float, y: Float, shadow: Boolean) {
        drawChat(chat, x, y, -1, shadow)
    }

    fun drawChat(chat: ITextComponent, x: Float, y: Float, color: Int) {
        this.drawChat(chat, x, y, color, true)
    }

    fun drawChat(chat: ITextComponent, x: Float, y: Float, color: Int, shadow: Boolean) {
        var y = y

        var x1 = x
        for (c in chat) {
            if (c is FancyText) {
                for (s in c.getString().lines().dropLastWhile { it.isEmpty() }) {
                    val length = fontRenderer.getStringWidth(s)
                    fill(x1.toInt(), y.toInt(), x1.toInt() + length, y.toInt() - fontRenderer.FONT_HEIGHT, c.fancyStyle.highlight!!.hex)
                    hLine(x1.toInt(), x1.toInt() + length, y.toInt() + fontRenderer.FONT_HEIGHT - 1, c.fancyStyle.underline!!.hex)
                }
            }
            x1 += fontRenderer.getStringWidth(c.unformattedComponentText).toFloat()
        }
        for (s in chat.string.lines().dropLastWhile { it.isEmpty() }) {
            if (shadow) {
                fontRenderer.drawStringWithShadow(s, x, y, color)
            } else {
                fontRenderer.drawString(s, x, y, color)
            }
            y += fontRenderer.FONT_HEIGHT.toFloat()
        }
    }

}
