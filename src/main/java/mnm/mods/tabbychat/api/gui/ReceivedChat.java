package mnm.mods.tabbychat.api.gui;

import net.minecraft.util.text.ITextComponent;

/**
 * This contains all the previously received chat from the current server.
 */
public interface ReceivedChat extends IGui {

    /**
     * Gets the {@link IChatComponent} that is at the specified coordinates.
     * Values should be relative to the screen, not the component.
     *
     * @param clickX The x position
     * @param clickY The y position
     * @return The chat component
     */
    ITextComponent getChatComponent(int clickX, int clickY);

    /**
     * Scrolls the chat the specified number of lines. Positive goes up,
     * negative goes down.
     *
     * @param scroll The number of lines to scroll
     */
    void scroll(int scroll);

    /**
     * Sets where the scroll position should be at. The value is adjusted for
     * invalid positions. Scroll must be >= 0 and <= the number of received
     * messages minus the height of the box.
     *
     * @param scroll The scroll pos
     */
    void setScrollPos(int scroll);

    /**
     * Gets the current scroll potision
     *
     * @return The scroll pos
     */
    int getScrollPos();

    /**
     * Resets the scroll position to 0. Same as calling {@code setScrollPos(0);}
     */
    void resetScroll();
}
