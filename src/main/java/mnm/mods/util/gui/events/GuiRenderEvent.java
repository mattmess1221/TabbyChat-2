package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

public class GuiRenderEvent extends AbstractMouseEvent {

    private float ticks;

    public GuiRenderEvent(GuiComponent component, int mouseX, int mouseY, float ticks) {
        super(component, mouseX, mouseY);
        this.ticks = ticks;
    }

    public float getTicks() {
        return ticks;
    }
}
