package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.util.mc
import kotlin.math.absoluteValue
import kotlin.math.max

class Scrollbar(private val chat: ChatArea) : GuiComponent() {

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        if (mc.ingameGUI.chatGUI.chatOpen) {
            val scroll = chat.scrollPos.toFloat()
            val max = chat.location.height.toFloat()
            val lines = max / mc.fontRenderer.FONT_HEIGHT
            var total = chat.chat.size.toFloat()
            if (total <= lines) {
                return
            }
            total -= lines
            val size = max(max / 2 - total, 10.toFloat())
            val perc = ((scroll / total - 1) * (size / max - 1)).absoluteValue
            val pos = (perc * max).toInt()

            val loc = location
            fill(loc.xPos, loc.yPos + pos, loc.xPos + 1, loc.yPos + pos + size.toInt(), -1)
            super.render(mouseX, mouseY, parTicks)
        }
    }

}
