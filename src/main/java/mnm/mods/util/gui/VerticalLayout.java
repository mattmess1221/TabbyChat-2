package mnm.mods.util.gui;

import java.awt.Dimension;
import java.util.List;

import com.google.common.collect.Lists;

import mnm.mods.util.Location;

/**
 * Displays components top to bottom. Like {@link FlowLayout}, but vertical.
 * 
 * @author Matthew
 */
public class VerticalLayout implements ILayout {

    private List<GuiComponent> list = Lists.newArrayList();

    @Override
    public void addComponent(GuiComponent comp, Object constraints) {
        if (constraints != null) {
            if (constraints instanceof Integer) {
                list.add((Integer) constraints, comp);
            } else {
                throw new IllegalArgumentException("Illegal constraint of type: "
                        + constraints.getClass().getName() + ". Only int accepted.");
            }
        } else {
            list.add(comp);
        }
    }

    @Override
    public void removeComponent(GuiComponent comp) {
        list.remove(comp);
    }

    @Override
    public void layoutComponents(GuiPanel parent) {
        int y = 0;
        for (GuiComponent comp : list) {
            Location loc = comp.getLocation().copy();
            loc.setXPos(0);
            loc.setYPos(y);
            comp.setLocation(loc);
            y += comp.getMinimumSize().height;
        }
    }

    @Override
    public Dimension getLayoutSize() {
        int width = 0;
        int height = 0;
        for (GuiComponent comp : list) {
            width = Math.max(width, comp.getLocation().getWidth());
            height += comp.getMinimumSize().height;
        }
        return new Dimension(width, height);
    }
}
