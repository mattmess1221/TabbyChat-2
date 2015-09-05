package mnm.mods.tabbychat.api.events;

import java.util.List;

import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.GuiChat;

public abstract class ScreenEvent extends Event {

    public final GuiChat chatScreen;

    public ScreenEvent(GuiChat screen) {
        this.chatScreen = screen;
    }

    /**
     * Event for when the chat screen gets initialized.
     */
    public static class ChatInitEvent extends ScreenEvent {

        public final List<GuiComponent> components;

        public ChatInitEvent(GuiChat chat, List<GuiComponent> components) {
            super(chat);
            this.components = components;
        }
    }

    /**
     * Called whenever {@link GuiChat#updateScreen()} is called.
     */
    public static class Update extends ScreenEvent {

        public Update(GuiChat chat) {
            super(chat);
        }
    }

    /**
     * Called when {@link GuiChat#onGuiClosed()} is called.
     */
    public static class Close extends ScreenEvent {

        public Close(GuiChat chat) {
            super(chat);
        }
    }

    /**
     * Called at the beginning of {@link GuiChat#drawScreen(int, int, float)}.
     */
    public static class Render extends ScreenEvent {

        public final int mouseX;
        public final int mouseY;
        public final float ticks;

        public Render(GuiChat chat, int mouseX, int mouseY, float tick) {
            super(chat);
            this.mouseX = mouseX;
            this.mouseY = mouseY;
            this.ticks = tick;
        }

    }
}
