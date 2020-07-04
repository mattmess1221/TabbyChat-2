package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.TexturedModal
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.audio.SimpleSound
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import org.lwjgl.opengl.GL11

/**
 * A [net.minecraft.client.gui.widget.button.Button] for the GuiComponent system.
 */
class GuiButton(override val text: String, val callback: (GuiButton) -> Unit = {}) : AbstractGuiButton() {

    override fun onPress() = callback(this)
}

abstract class AbstractGuiButton() : GuiComponent() {

    var sound: SoundEvent? = null

    abstract val text: String

    override var minimumSize: Dim
        get() = Dim(mc.fontRenderer.getStringWidth(this.text) + 8, 20)
        set(value) {
            super.minimumSize = value
        }

    abstract fun onPress()

    override fun onClick(x: Double, y: Double) {
        sound?.play(1.0f)
        onPress()
    }

    fun SoundEvent.play(volume: Float) {
        mc.soundHandler.play(SimpleSound.master(this, volume))
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        val fontrenderer = mc.fontRenderer
        val bounds = location

        RenderSystem.pushMatrix()

        mc.getTextureManager().bindTexture(WIDGETS)
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)

        val hovered = bounds.contains(x, y)

        val modal = this.getHoverState(hovered)
        RenderSystem.enableBlend()
        RenderSystem.blendFuncSeparate(770, 771, 1, 0)
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        this.drawModalCorners(modal)

        var textColor = 0xE0E0E0

        if (!this.active) {
            textColor = 0xA0A0A0
        } else if (hovered) {
            textColor = 0xFFFFA0
        }

        this.drawCenteredString(fontrenderer, text, bounds.xCenter, bounds.yCenter - fontrenderer.FONT_HEIGHT / 2, textColor)

        RenderSystem.popMatrix()
    }

    private fun getHoverState(hovered: Boolean): TexturedModal {
        if (!this.active) {
            return MODAL_DISABLE
        }
        if (hovered) {
            return MODAL_HOVER
        }

        return MODAL_NORMAL
    }

    companion object {

        internal val WIDGETS = ResourceLocation("textures/gui/widgets.png")
        private val MODAL_NORMAL = TexturedModal(WIDGETS, 0, 66, 200, 20)
        private val MODAL_HOVER = TexturedModal(WIDGETS, 0, 86, 200, 20)
        private val MODAL_DISABLE = TexturedModal(WIDGETS, 0, 46, 200, 20)
    }

}
