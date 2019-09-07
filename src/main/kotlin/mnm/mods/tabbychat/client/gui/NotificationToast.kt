package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.platform.GlStateManager
import net.minecraft.client.gui.toasts.IToast
import net.minecraft.client.gui.toasts.ToastGui
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextFormatting
import org.lwjgl.opengl.GL11

class NotificationToast(private val owner: String, title: ITextComponent) : IToast {
    private val title: String = title.string

    private var firstDrawTime: Long = 0
    private var newDisplay: Boolean = false

    override fun draw(toastGui: ToastGui, delta: Long): IToast.Visibility {
        if (this.newDisplay) {
            this.firstDrawTime = delta
            this.newDisplay = false
        }
        var x = 10
        val textWidth = toastGui.minecraft.fontRenderer.getStringWidth(title)
        val delay: Long = 500
        val maxSize = textWidth - 150
        val timeElapsed = delta - firstDrawTime - delay
        if (timeElapsed > 0 && textWidth > maxSize) {
            x = Math.max((-maxSize * timeElapsed / 8000L + x).toInt(), -maxSize)
        }

        toastGui.minecraft.getTextureManager().bindTexture(IToast.TEXTURE_TOASTS)
        GlStateManager.color3f(1.0f, 1.0f, 1.0f)
        toastGui.blit(0, 0, 0, 0, 160, 32)

        toastGui.minecraft.fontRenderer.drawString(TextFormatting.UNDERLINE.toString() + this.owner, 8.0f, 6.0f, -256)

        val window = toastGui.minecraft.mainWindow
        val height = window.scaledHeight.toDouble()
        val scale = window.guiScaleFactor

        val trans = FloatArray(16)
        GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, trans)
        val xpos = trans[12]

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(((xpos + 10) * scale).toInt(), ((height - 32) * scale).toInt(), (140 * scale).toInt(), (32 * scale).toInt())

        toastGui.minecraft.fontRenderer.drawString(this.title, x.toFloat(), 16.0f, -1)

        GL11.glDisable(GL11.GL_SCISSOR_TEST)

        return if (delta - this.firstDrawTime < 10000L) IToast.Visibility.SHOW else IToast.Visibility.HIDE
    }

    override fun getType(): String {
        return this.owner
    }
}
