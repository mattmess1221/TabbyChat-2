package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A panel wrapper for a screen.
 */
public class ComponentScreen extends Screen {

    private final GuiPanel PANEL = new GuiPanel();

    public ComponentScreen(ITextComponent title) {
        super(title);
    }

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
    public List<? extends IGuiEventListener> children() {
        return PANEL.children();
    }

    @Override
    public void init(Minecraft mc, int width, int height) {
        PANEL.setLocation(new Location(0, 0, width, height));
        PANEL.clearComponents();
        super.init(mc, width, height);
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
