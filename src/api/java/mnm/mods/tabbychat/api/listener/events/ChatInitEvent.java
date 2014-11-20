package mnm.mods.tabbychat.api.listener.events;

import java.util.List;

import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.GuiButton;

/**
 * Event for when the chat screen gets initialized.
 */
public class ChatInitEvent extends Event {
    public final List<GuiButton> buttons;
    public final List<GuiComponent> components;

    public ChatInitEvent(List<GuiButton> buttons, List<GuiComponent> components) {
        this.buttons = buttons;
        this.components = components;
    }
}
