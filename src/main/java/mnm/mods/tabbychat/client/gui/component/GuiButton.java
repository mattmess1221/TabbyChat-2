package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.TexturedModal;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link net.minecraft.client.gui.GuiButton} for the GuiComponent system.
 */
public class GuiButton extends GuiComponent {

    protected static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
    private static final TexturedModal MODAL_NORMAL = new TexturedModal(WIDGETS, 0, 66, 200, 20);
    private static final TexturedModal MODAL_HOVER = new TexturedModal(WIDGETS, 0, 86, 200, 20);
    private static final TexturedModal MODAL_DISABLE = new TexturedModal(WIDGETS, 0, 46, 200, 20);

    private String text = "";
    private SoundEvent sound;

    /**
     * Instantiates a new button with {@code text} as the display string.
     *
     * @param text The display string
     */
    public GuiButton(String text) {
        this.setText(text);
        setSound(SoundEvents.UI_BUTTON_CLICK);
    }

    protected void onClick(double mouseX, double mouseY) {
    }

    protected void onRelease(double mouseX, double mouseY) {
    }

    protected void onDrag(double mouseX, double mouseY, double mouseDX, double mouseDY) {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isPressable(mouseX, mouseY)) {
            this.playPressSound(mc.getSoundHandler());
            this.onClick(mouseX, mouseY);
            return true;
        }
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.onRelease(mouseX, mouseY);
            return true;
        }
        return false;

    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXDelta, double mouseYDelta) {
        if (button == 0) {
            this.onDrag(mouseX, mouseY, mouseXDelta, mouseYDelta);
            return true;
        }
        return false;

    }

    protected boolean isPressable(double mouseX, double mouseY) {
        return this.isEnabled() && this.isVisible() && this.getLocation().contains(mouseX, mouseY);
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
        SoundEvent sound = getSound();
        if (sound != null) {
            soundHandlerIn.play(SimpleSound.master(sound, 1.0F));
        }
    }

    /**
     * Sets the display text for this button.
     *
     * @param text The new text
     */
    public void setText(@Nullable String text) {
        if (text == null) {
            text = "";
        }
        this.text = text;
    }

    /**
     * Gets the display text for this button.
     *
     * @return The text
     */
    public String getText() {
        return this.text;
    }

    public void setSound(@Nullable SoundEvent sound) {
        this.sound = sound;
    }

    @Nullable
    public SoundEvent getSound() {
        return sound;
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        FontRenderer fontrenderer = mc.fontRenderer;
        ILocation bounds = getLocation();

        GlStateManager.pushMatrix();

        mc.getTextureManager().bindTexture(WIDGETS);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        boolean hovered = bounds.contains(mouseX, mouseY);

        TexturedModal modal = this.getHoverState(hovered);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.drawModalCorners(modal);

        int textColor = 0xE0E0E0;

        if (!this.isEnabled()) {
            textColor = 0xA0A0A0;
        } else if (hovered) {
            textColor = 0xFFFFA0;
        }

        int x = bounds.getXCenter();
        int y = bounds.getYCenter() - fontrenderer.FONT_HEIGHT / 2;

        this.drawCenteredString(fontrenderer, getText(), x, y, textColor);

        GlStateManager.popMatrix();
    }

    private TexturedModal getHoverState(boolean hovered) {
        TexturedModal modal = GuiButton.MODAL_NORMAL;

        if (!this.isEnabled()) {
            modal = GuiButton.MODAL_DISABLE;
        } else if (hovered) {
            modal = GuiButton.MODAL_HOVER;
        }

        return modal;
    }

    @Nonnull
    @Override
    public Dim getMinimumSize() {
        return new Dim(mc.fontRenderer.getStringWidth(this.getText()) + 8, 20);
    }

}
