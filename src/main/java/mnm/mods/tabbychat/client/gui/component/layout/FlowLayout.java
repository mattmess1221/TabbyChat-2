package mnm.mods.tabbychat.client.gui.component.layout;

import com.google.common.collect.Lists;
import mnm.mods.tabbychat.client.gui.component.GuiComponent;
import mnm.mods.tabbychat.client.gui.component.GuiPanel;
import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;

import java.util.List;

/**
 * A layout that puts items side-by-side and left-to-right.
 *
 * @author Matthew
 */
public class FlowLayout implements ILayout {

    private List<GuiComponent> components = Lists.newArrayList();

    @Override
    public void addComponent(GuiComponent comp, Object constraints) {
        components.add(comp);
    }

    @Override
    public void removeComponent(GuiComponent comp) {
        components.remove(comp);
    }

    @Override
    public void layoutComponents(GuiPanel parent) {

        ILocation loc = parent.getLocation();
        int xPos = loc.getXPos();
        int yPos = loc.getYPos();
        int maxH = 0;
        for (GuiComponent comp : components) {
            Dim size = comp.getMinimumSize();
            if (xPos + size.width > loc.getXWidth()) {
                // wrapping
                xPos = loc.getXPos();
                yPos += maxH;
                maxH = 0;
            }
            comp.setLocation(new Location(xPos, yPos, size.width, size.height));

            maxH = Math.max(maxH, size.height);
            xPos += size.width;
        }

    }

    @Override
    public Dim getLayoutSize() {
        int width = 0;
        int height = 0;

        for (GuiComponent comp : components) {
            ILocation loc = comp.getLocation();
            width += loc.getWidth();
            height = Math.max(height, loc.getHeight());
        }
        return new Dim(width, height);
    }

}
