package mnm.mods.util.gui;

import com.google.common.eventbus.Subscribe;
import mnm.mods.util.ILocation;
import mnm.mods.util.TexturedModal;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

import java.awt.Dimension;
import javax.annotation.Nonnull;

/**
 * A {@link net.minecraft.client.gui.GuiButton} for the GuiComponent system.
 */
public class GuiButton extends GuiComponent {

    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");
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

    @Subscribe
    public void onClick(ActionPerformedEvent action) {
        SoundEvent sound = getSound();
        if (sound != null) {
            mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, 1.0F));
        }
    }

    /**
     * Sets the display text for this button.
     *
     * @param text The new text
     */
    public void setText(String text) {
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

    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    public SoundEvent getSound() {
        return sound;
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        FontRenderer fontrenderer = mc.fontRenderer;
        ILocation bounds = getLocation();

        mc.getTextureManager().bindTexture(WIDGETS);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        TexturedModal modal = this.getHoverState(isHovered());
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.drawModalCorners(modal);

        int textColor = 0xE0E0E0;

        if (!this.isEnabled()) {
            textColor = 0xA0A0A0;
        } else if (this.isHovered()) {
            textColor = 0xFFFFA0;
        }

        this.drawCenteredString(fontrenderer, getText(), bounds.getWidth() / 2, (bounds.getHeight() - 8) / 2,
                textColor);

        super.drawComponent(mouseX, mouseY);
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
    public Dimension getMinimumSize() {
        return new Dimension(mc.fontRenderer.getStringWidth(this.getText()) + 8, 20);
    }

}
