package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.client.config.GuiUtils

/**
 * A checkbox, representing a boolean input.
 *
 * @author Matthew
 */
class GuiCheckbox : GuiButton(""), IGuiInput<Boolean> {

    override var value: Boolean = false

    init {
        location = Location(0, 0, 9, 9)
        secondaryColor = Color(-0x66000060)
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        val mc = Minecraft.getInstance()

        val loc = location
        GuiUtils.drawContinuousTexturedBox(WIDGETS, loc.xPos, loc.yPos, 0, 46, loc.width, loc.height, 200, 20, 2, 3, 2, 2, this.blitOffset.toFloat())

        if (value) {
            this.drawCenteredString(mc.fontRenderer, "x", loc.xCenter + 1, loc.yPos + 1, secondaryColorProperty.hex)
        }
        this.drawString(mc.fontRenderer, text, loc.xWidth + 2, loc.yPos + 2, primaryColorProperty.hex)
    }

    override fun onClick(mouseX: Double, mouseY: Double) {
        value = !value
    }
}
