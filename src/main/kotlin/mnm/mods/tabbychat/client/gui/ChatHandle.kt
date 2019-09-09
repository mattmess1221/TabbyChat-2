package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.mc

class ChatHandle internal constructor() : GuiComponent() {

    init {
        location = Location(0, 0, 10, 10)
        minimumSize = Dim(12, 12)
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        val loc = location
        val color = getColor(loc.contains(x, y)).hex
        hLine(loc.xPos + 3, loc.xWidth - 4, loc.yPos + 4, color)
        vLine(loc.xWidth - 4, loc.yPos + 4, loc.yHeight - 2, color)
    }

    private fun getColor(hovered: Boolean): Color {
        val opac = (mc.gameSettings.chatOpacity * 255).toInt()
        return Color(255, 255, if (hovered) 160 else 255, opac)
    }
}
