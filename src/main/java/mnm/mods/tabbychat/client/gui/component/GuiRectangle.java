package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.ILocation;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Represents a colored area. Transparency is represented using a transparency
 * grid.
 *
 * @author Matthew
 */
public class GuiRectangle extends GuiComponent {

    private static final ResourceLocation TRANSPARENCY = new ResourceLocation("mnmutils", "textures/transparency.png");

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(TRANSPARENCY);
        ILocation loc = getLocation();
        Gui.drawModalRectWithCustomSizedTexture(loc.getXPos(), loc.getYPos(), 0, 0, loc.getWidth(), loc.getHeight(), 5, 5);
        Gui.drawRect(loc.getXPos(), loc.getYPos(), loc.getXWidth(), loc.getYHeight(), getPrimaryColorProperty().getHex());
        GlStateManager.disableBlend();
    }
}
