package mnm.mods.tabbychat.api.gui;

import mnm.mods.util.gui.GuiPanel;

/**
 * The root chat box contains all the components that make up the individual
 * parts.
 */
public interface ChatGui extends IGui<GuiPanel> {

    /**
     * Gets the input portion of the chat box.
     *
     * @return The text box
     */
    ChatInput getChatInput();

    /**
     * Gets the tray of the chat box, which contains the tabs and resize handle.
     *
     * @return The tray
     */
    Tray getTray();

    /**
     * Gets the area that contains all the received chat.
     *
     * @return The chat
     */
    ReceivedChat getChatArea();
}
