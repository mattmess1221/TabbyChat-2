package mnm.mods.util.gui;

import static org.lwjgl.opengl.GL11.*;

import com.google.common.eventbus.Subscribe;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import mnm.mods.util.gui.BorderLayout.Position;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;

import java.awt.Dimension;
import javax.annotation.Nonnull;

/**
 * TODO: Horizontal scrolling
 *
 * @author Matthew
 */
public class GuiScrollingPanel extends GuiPanel {

    private GuiPanel panel;

    public GuiScrollingPanel() {
        super(new BorderLayout());
        this.panel = new GuiPanel();
        this.panel.setLocation(new Location(0, 0, 100000, 100000));
        GuiPanel panel = new GuiPanel();
        panel.addComponent(this.panel);
        this.addComponent(panel, Position.CENTER);
        this.addComponent(new Scrollbar(), Position.EAST);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        ILocation actual = getActualLocation();
        ILocation rect = getLocation();

        glEnable(GL_SCISSOR_TEST);
        glScissor(actual.getXPos() * 2, mc.displayHeight - rect.getHeight() * 2 - actual.getYPos() * 2, rect.getWidth() * 2, rect.getHeight() * 2);

        super.drawComponent(mouseX, mouseY);

        glDisable(GL_SCISSOR_TEST);
    }

    @Subscribe
    public void scroll(GuiMouseEvent event) {
        if (event.getType() == MouseEvent.SCROLL) {
            Location rect = panel.getLocation().copy();
            int scr = rect.getYPos() + event.getScroll() / 12;
            rect.setYPos(scr);

            panel.getParent().map(GuiComponent::getLocation).ifPresent(prect -> {
                Dimension dim = panel.getMinimumSize();
                if (rect.getYPos() + dim.height < prect.getHeight()) {
                    rect.setYPos(prect.getHeight() - dim.height);
                }
            });
            if (rect.getYPos() > 0)
                rect.setYPos(0);

            panel.setLocation(rect);
        }
    }

    public GuiPanel getContentPanel() {
        return panel;
    }

    @Nonnull
    @Override
    public Dimension getMinimumSize() {
        return getLocation().getSize();
    }

    // TODO Make draggable
    private class Scrollbar extends GuiComponent {

        @Override
        public void drawComponent(int mouseX, int mouseY) {
            int scroll = panel.getLocation().getYPos();
            int max = GuiScrollingPanel.this.getLocation().getHeight();
            int total = panel.getMinimumSize().height;
            if (total <= max) {
                return;
            }
            total -= max;
            Gui.drawRect(0, 20, 10, 10, -1);
            int size = Math.max(max / 2, 10);
            float perc = ((float) scroll / (float) total) * ((float) size / (float) max);
            int pos = (int) (-perc * max);

            Gui.drawRect(-1, pos, 0, pos + size - 1, -1);
            super.drawComponent(mouseX, mouseY);
        }
    }
}
