package mnm.mods.tabbychat.api.events;

import net.minecraft.client.gui.GuiChat;

/**
 * Contains mouse and keyboard events in GuiChat
 */
public abstract class InputEvent extends ScreenEvent {

    public InputEvent(GuiChat screen) {
        super(screen);
    }

    /**
     * Event for mouse clicks in GuiChat.
     */
    public static class MouseClick extends InputEvent {
        public final int mouseX;
        public final int mouseY;
        public final int button;

        public MouseClick(GuiChat chat, int x, int y, int button) {
            super(chat);
            this.mouseX = x;
            this.mouseY = y;
            this.button = button;
        }
    }

    /**
     * Event for key typed in GuiChat
     */
    public static class KeyTyped extends InputEvent {
        public final char character;
        public final int keyId;

        public KeyTyped(GuiChat chat, char ch, int code) {
            super(chat);
            this.character = ch;
            this.keyId = code;
        }
    }
}
