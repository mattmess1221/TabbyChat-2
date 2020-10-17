package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Colors
import mnm.mods.tabbychat.util.argb
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.IRenderable

class ChatHandle(private val chat: ChatBox) : AbstractGui(), IGuiEventListener, IRenderable, ILocatable {

   override val xPos get() = chat.xPos + chat.width - width - 1
   override val yPos get() = chat.yPos + 1
   override val width get() = chat.tray.height - 2
   override val height get() = chat.tray.height - 2

    override fun render(x: Int, y: Int, parTicks: Float) {
        val color = getColor(this.contains(x, y))
        hLine(xPos + 3, xPos + width - 4, yPos + 4, color)
        vLine(xPos + width - 4, yPos + 4, yPos + height - 2, color)
    }

    private fun getColor(hovered: Boolean): Color {
        val opac = (mc.gameSettings.chatOpacity * 255).toInt()
        return argb(opac, 255, 255, if (hovered) 160 else 255)
    }
}
