package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.Dim;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;

import java.util.IdentityHashMap;
import java.util.Map;

public class AbsoluteLayout implements ILayout {

    private Map<GuiComponent, ILocation> components = new IdentityHashMap<>();

    @Override
    public void addComponent(GuiComponent comp, Object constraints) {
        if (constraints != null && !(constraints instanceof ILocation)) {
            throw new IllegalArgumentException("Nonnull constraint must be ILocation");
        }
        ILocation loc = (ILocation) constraints;
        if (loc == null) {
            loc = new Location();
        }
        components.put(comp, loc.asImmutable());
    }

    @Override
    public void removeComponent(GuiComponent comp) {
        components.remove(comp);
    }

    @Override
    public void layoutComponents(GuiPanel parent) {
        ILocation ploc = parent.getLocation();
        for (Map.Entry<GuiComponent, ILocation> e : components.entrySet()) {
            GuiComponent comp = e.getKey();
            Location loc = e.getValue().copy();
            comp.setLocation(loc.move(ploc.getXPos(), ploc.getYPos()));
        }
    }

    @Override
    public Dim getLayoutSize() {
        int width = 0;
        int height = 0;
        for (Map.Entry<GuiComponent, ILocation> e : components.entrySet()) {
            GuiComponent comp = e.getKey();
            ILocation loc = e.getValue();
            width = Math.max(width, comp.getLocation().getWidth());
            height = Math.max(height, comp.getLocation().getHeight());
        }
        return new Dim(width, height);
    }
}
