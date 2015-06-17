package mnm.mods.tabbychat.api.listener.events;

import java.util.List;

import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.GuiChat;

/**
 * Event for when the chat screen gets initialized.
 */
public class ChatInitEvent extends Event {

    public final GuiChat chatScreen;
    public final List<GuiComponent> components;

    public ChatInitEvent(GuiChat chat, List<GuiComponent> components) {
        this.chatScreen = chat;
        this.components = components;
    }
}
