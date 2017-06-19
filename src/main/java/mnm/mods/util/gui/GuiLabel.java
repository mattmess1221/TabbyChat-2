package mnm.mods.util.gui;

import mnm.mods.util.text.FancyFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;

/**
 * Gui component label used to show text on the screen.
 *
 * @author Matthew
 */
public class GuiLabel extends GuiComponent {

    private FancyFontRenderer fr;
    private ITextComponent text;
    private float angle;

    public GuiLabel() {
        this.fr = new FancyFontRenderer(Minecraft.getMinecraft().fontRenderer);
    }

    /**
     * Creates a label from a chat component.
     *
     * @param chat The text
     */
    public GuiLabel(ITextComponent chat) {
        this();
        this.setText(chat);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        if (getText() == null)
            return;
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 0, 0, angle);
        if (angle < 180) {
            GlStateManager.translate(-angle / 1.5, -angle / 4, 0);
        } else {
            GlStateManager.translate(-angle / 15, angle / 40, 0);
        }

        fr.drawChat(getText(), 0, 1, getPrimaryColorProperty().getHex(), true);

        GlStateManager.popMatrix();
        super.drawComponent(mouseX, mouseY);
    }

    /**
     * Sets the string of this label
     *
     * @param text The string
     */
    public void setText(ITextComponent text) {
        this.text = text;
    }

    /**
     * Gets the string of this label
     *
     * @return The string
     */
    public ITextComponent getText() {
        return text;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

}
