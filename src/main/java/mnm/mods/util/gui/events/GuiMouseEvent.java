package mnm.mods.util.gui.events;

import mnm.mods.util.gui.GuiComponent;

/**
 * A event for the mouse adapter.
 *
 * @author Matthew
 */
public class GuiMouseEvent extends AbstractMouseEvent {

    private MouseEvent type;
    private int button;
    private long buttonTime;
    private int scroll;

    public GuiMouseEvent(GuiComponent component, MouseEvent event, int mouseX, int mouseY, int button, long buttonTime) {
        this(component, event, mouseX, mouseY, button, buttonTime, 0);
    }

    public GuiMouseEvent(GuiComponent component, MouseEvent type, int mouseX, int mouseY, int button, long buttonTime, int scroll) {
        super(component, mouseX, mouseY);
        this.type = type;
        this.button = button;
        this.scroll = scroll;
    }

    public MouseEvent getType() {
        return type;
    }

    public int getButton() {
        return button;
    }
    
    public long getButtonTime() {
        return buttonTime;
    }

    public int getScroll() {
        return scroll;
    }

    public enum MouseEvent {
        RAW,
        CLICK,
        RELEASE,
        SCROLL,
        DRAG,;
    }
}
