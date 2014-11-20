package mnm.mods.tabbychat.api;

import net.minecraft.util.IChatComponent;

/**
 * Represents a message.
 */
public interface Message {

    /**
     * Gets the message
     *
     * @return The message
     */
    IChatComponent getMessage();

    /**
     * Gets the update counter used for this message.
     *
     * @return The counter
     */
    int getCounter();

    /**
     * Gets the ID of this message. 0 is a normal message.
     *
     * @return The ID of this message
     */
    int getID();

    /**
     * Gets the channels defined in this message.
     *
     * @return The channels
     */
    Channel[] getChannels();

    /**
     * Adds a channel (or channels) to this message.
     *
     * @param channels The new channels
     */
    void addChannel(Channel... channels);

    /**
     * Gets whether any channel in this message is active.
     *
     * @return True if active
     */
    boolean isActive();

}
