package mnm.mods.tabbychat.client.gui.settings

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.client.gui.component.AbstractGuiButton
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.mc

class SettingsButton internal constructor(val settings: SettingPanel<*>, val callback: (SettingPanel<*>) -> Unit = {}) : AbstractGuiButton() {

    override val text: String = settings.displayString
    var displayed = false

    private var displayX = 30

    init {
        location = Location(0, 0, 75, 20)
        minimumSize = location.size
        secondaryColor = settings.secondaryColor
    }

    override fun onPress() = callback(settings)

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (displayed && displayX > 20) {
            displayX -= 2
        } else if (!displayed && displayX < 30) {
            displayX += 2
        }
        val loc = this.location
        val x1 = loc.xPos + displayX - 30
        val x2 = loc.xWidth + displayX - 30
        val y1 = loc.yPos + 1
        val y2 = loc.yHeight - 1

        GlStateManager.enableAlphaTest()
        fill(x1, y1, x2, y2, secondaryColor!!.hex)
        val string = mc.fontRenderer.trimStringToWidth(text, loc.width)
        mc.fontRenderer.drawString(string, (x1 + 10).toFloat(), (loc.yCenter - 4).toFloat(), primaryColorProperty.hex)
    }
}
