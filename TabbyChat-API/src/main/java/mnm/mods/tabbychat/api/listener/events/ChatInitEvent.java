package mnm.mods.tabbychat.api.listener.events;

import java.util.List;

import mnm.mods.util.gui.GuiComponent;

/**
 * Event for when the chat screen gets initialized.
 */
public class ChatInitEvent extends Event {
    public final List<GuiComponent> components;

    public ChatInitEvent(List<GuiComponent> components) {
        this.components = components;
    }
}
