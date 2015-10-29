package mnm.mods.tabbychat.api.listener.events;

import java.util.List;

import net.minecraft.client.gui.GuiChat;

/**
 * Event for when the chat screen gets initialized.
 */
public class ChatInitEvent extends Event {

    public final GuiChat chatScreen;
    public final List<?> components;

    public ChatInitEvent(GuiChat chat, List<?> components) {
        this.chatScreen = chat;
        this.components = components;
    }
}
