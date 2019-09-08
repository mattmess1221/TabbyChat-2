package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.platform.GLX
import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.TexturedModal
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.audio.SimpleSound
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.gui.AbstractGui
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import org.lwjgl.opengl.GL11

/**
 * A [net.minecraft.client.gui.widget.button.Button] for the GuiComponent system.
 */
class GuiButton(override val text: String, val callback: () -> Unit = {}) : AbstractGuiButton() {

    override fun onPress() = callback()
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

    override fun onClick(x: Double, y: Double) = onPress()

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        val fontrenderer = mc.fontRenderer
        val bounds = location

        GlStateManager.pushMatrix()

        mc.getTextureManager().bindTexture(WIDGETS)
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f)

        val hovered = bounds.contains(mouseX, mouseY)

        val modal = this.getHoverState(hovered)
        GlStateManager.enableBlend()
        GLX.glBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        this.drawModalCorners(modal)

        var textColor = 0xE0E0E0

        if (!this.active) {
            textColor = 0xA0A0A0
        } else if (hovered) {
            textColor = 0xFFFFA0
        }

        val x = bounds.xCenter
        val y = bounds.yCenter - fontrenderer.FONT_HEIGHT / 2

        this.drawCenteredString(fontrenderer, text, x, y, textColor)

        GlStateManager.popMatrix()
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
