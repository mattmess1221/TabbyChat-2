package mnm.mods.tabbychat.api.events;

import net.minecraft.client.gui.GuiChat;

public abstract class ChatScreenEvents {

    public final GuiChat chatScreen;

    public ChatScreenEvents(GuiChat chat) {
        this.chatScreen = chat;
    }

    /**
     * Event for when the chat screen gets initialized.
     */
    public static class ChatInitEvent extends ChatScreenEvents {

        public ChatInitEvent(GuiChat chat) {
            super(chat);
        }
    }

    public static class ChatUpdateEvent extends ChatScreenEvents {

        public ChatUpdateEvent(GuiChat chat) {
            super(chat);
        }
    }

    public static class ChatCloseEvent extends ChatScreenEvents {

        public ChatCloseEvent(GuiChat chat) {
            super(chat);
        }
    }    
}