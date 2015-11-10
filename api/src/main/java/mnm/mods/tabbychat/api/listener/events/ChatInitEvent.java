package mnm.mods.tabbychat.api.listener.events;

import net.minecraft.client.gui.GuiChat;

/**
 * Event for when the chat screen gets initialized.
 */
public class ChatInitEvent extends Event {

    public final GuiChat chatScreen;

    public ChatInitEvent(GuiChat chat) {
        this.chatScreen = chat;
    }
}
