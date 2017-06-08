package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * Abstract class for events that contain the current mouse coordinates.
 */
abstract class AbstractMouseEvent extends GuiEvent {

    private int mouseX;
    private int mouseY;

    AbstractMouseEvent(GuiComponent component, int mouseX, int mouseY) {
        super(component);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
