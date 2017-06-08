package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * A class for the gui events.
 *
 * @author Matthew
 */
public abstract class GuiEvent {

    private GuiComponent component;

    public GuiEvent(GuiComponent component) {
        this.component = component;
    }

    public GuiComponent getComponent() {
        return component;
    }
}
