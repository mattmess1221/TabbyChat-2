package mnm.mods.tabbychat.client.gui.component;

import com.mojang.blaze3d.platform.GlStateManager;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.text.FancyFontRenderer;
import net.minecraft.client.Minecraft;
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
        this.fr = new FancyFontRenderer(Minecraft.getInstance().fontRenderer);
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
    public void render(int mouseX, int mouseY, float parTicks) {
        if (getText() == null)
            return;
        GlStateManager.pushMatrix();

        GlStateManager.rotatef(angle, 0, 0, angle);
        if (angle < 180) {
            GlStateManager.translated(-angle / 1.5, -angle / 4, 0);
        } else {
            GlStateManager.translated(-angle / 15, angle / 40, 0);
        }

        ILocation loc = getLocation();
        fr.drawChat(getText(), loc.getXPos() + 3, loc.getYPos() + 3, getPrimaryColorProperty().getHex(), true);

        GlStateManager.popMatrix();
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
