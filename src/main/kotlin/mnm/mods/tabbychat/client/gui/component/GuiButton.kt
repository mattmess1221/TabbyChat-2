package mnm.mods.tabbychat.client.gui.component

import com.mojang.blaze3d.platform.GLX
import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.TexturedModal
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.audio.SimpleSound
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.gui.FontRenderer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.SoundEvents
import org.lwjgl.opengl.GL11
import java.util.Optional

/**
 * A [net.minecraft.client.gui.widget.button.Button] for the GuiComponent system.
 */
open class GuiButton(open var text: String = "") : GuiComponent() {

    var sound: SoundEvent? = SoundEvents.UI_BUTTON_CLICK

    override var minimumSize: Dim
        get() = Dim(mc.fontRenderer.getStringWidth(this.text) + 8, 20)
        set(value) {
            super.minimumSize = value
        }

    override fun playDownSound(soundHandlerIn: SoundHandler) {
        val sound = sound
        if (sound != null) {
            soundHandlerIn.play(SimpleSound.master(sound, 1.0f))
        }
    }

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

        if (!this.isEnabled) {
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
        if (!this.isEnabled) {
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
