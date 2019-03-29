package mnm.mods.util.gui;

import mnm.mods.util.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.IGuiEventListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A panel wrapper for a screen.
 */
public class ComponentScreen extends GuiScreen {

    private final GuiPanel PANEL = new GuiPanel();

    @Override
    public void render(int mouseX, int mouseY, float tick) {
        PANEL.render(mouseX, mouseY, tick);
        PANEL.drawCaption(mouseX, mouseY);
    }

    @Override
    public void tick() {
        PANEL.tick();
    }

    @Override
    @Nonnull
    public List<? extends IGuiEventListener> getChildren() {
        return PANEL.getChildren();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        PANEL.setLocation(new Location(0, 0, width, height));
        PANEL.clearComponents();
        super.setWorldAndResolution(mc, width, height);
    }

    /**
     * Gets the main panel on this screen. Add things to this.
     * 
     * @return The main panel
     */
    protected GuiPanel getPanel() {
        return PANEL;
    }
}
