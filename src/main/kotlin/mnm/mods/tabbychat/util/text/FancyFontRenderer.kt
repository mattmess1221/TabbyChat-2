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
        var column = x
        for (c in chat) {
            if (c is FancyText) {
                for (s in c.getString().lines().dropLastWhile { it.isEmpty() }) {
                    val length = fontRenderer.getStringWidth(s)
                    fill(column.toInt(), y.toInt(), column.toInt() + length, y.toInt() - fontRenderer.FONT_HEIGHT, c.fancyStyle.highlight ?: 0)
                    hLine(column.toInt(), column.toInt() + length, y.toInt() + fontRenderer.FONT_HEIGHT - 1, c.fancyStyle.underline ?: 0)
                }
            }
            column += fontRenderer.getStringWidth(c.unformattedComponentText).toFloat()
        }

        var line = y
        for (s in chat.string.lines().dropLastWhile { it.isEmpty() }) {
            if (shadow) {
                fontRenderer.drawStringWithShadow(s, x, line, color)
            } else {
                fontRenderer.drawString(s, x, line, color)
            }
            line += fontRenderer.FONT_HEIGHT.toFloat()
        }
    }

}
