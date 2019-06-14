package mnm.mods.tabbychat.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class NotificationToast implements IToast {

    private String owner;
    private String title;

    private long firstDrawTime;
    private boolean newDisplay;

    public NotificationToast(String owner, ITextComponent title) {
        this.owner = owner;
        this.title = title.getString();
    }

    @Nonnull
    public IToast.Visibility draw(@Nonnull ToastGui toastGui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }
        int x = 10;
        int textWidth = toastGui.getMinecraft().fontRenderer.getStringWidth(title);
        final long delay = 500;
        int maxSize = textWidth - 150;
        long timeElapsed = delta - firstDrawTime - delay;
        if (timeElapsed > 0 && textWidth > maxSize) {
            x = Math.max((int) (-maxSize * (timeElapsed) / (8000L) + x), -maxSize);
        }

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        toastGui.blit(0, 0, 0, 0, 160, 32);

        toastGui.getMinecraft().fontRenderer.drawString(TextFormatting.UNDERLINE + this.owner, 8.0F, 6.0F, -256);

        MainWindow window = toastGui.getMinecraft().mainWindow;
        int w = window.getWidth();
        int h = window.getHeight();
        double s = window.getGuiScaleFactor();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (w - 160 * s) + 10, (int) (h - 32 * s), (int) (160 * s) - 20, (int) (32 * s));

        toastGui.getMinecraft().fontRenderer.drawString(this.title, x, 16.0F, -1);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        return delta - this.firstDrawTime < 10000L ? IToast.Visibility.SHOW : IToast.Visibility.HIDE;
    }

    @Override
    @Nonnull
    public String getType() {
        return this.owner;
    }
}
