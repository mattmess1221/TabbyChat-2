package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.IRenderable
import kotlin.math.absoluteValue
import kotlin.math.max

class Scrollbar(private val chat: ChatArea) : AbstractGui(), IRenderable {

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (mc.ingameGUI.chatGUI.chatOpen) {
            val scroll = chat.scrollPos.toFloat()
            val max = chat.height.toFloat()
            val lines = max / mc.fontRenderer.FONT_HEIGHT
            var total = chat.chat.size.toFloat()
            if (total <= lines) {
                return
            }
            total -= lines
            val size = max(max / 2 - total, 10.toFloat())
            val perc = ((scroll / total - 1) * (size / max - 1)).absoluteValue
            val pos = (perc * max).toInt()

            fill(chat.xPos + chat.width,
                    chat.yPos + pos,
                    chat.xPos + chat.width + 1,
                    chat.yPos + pos + size.toInt(), -1)
        }
    }

}
