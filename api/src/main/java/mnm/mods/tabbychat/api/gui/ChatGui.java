package mnm.mods.tabbychat.api.gui;

/**
 * The root chat box contains all the components that make up the individual
 * parts.
 */
public interface ChatGui<Panel> extends IGui<Panel> {

    /**
     * Gets the input portion of the chat box.
     *
     * @return The text box
     */
    ChatInput<?, ?> getChatInput();

    /**
     * Gets the tray of the chat box, which contains the tabs and resize handle.
     *
     * @return The tray
     */
    IGui<?> getTray();

    /**
     * Gets the area that contains all the received chat.
     *
     * @return The chat
     */
    ReceivedChat<?> getChatArea();
}
