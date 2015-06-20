package mnm.mods.tabbychat.api;

/**
 * Represents the Chat.
 */
public interface Chat {

    /**
     * Gets a {@link Channel} of the given name. Creates a new one if it doesn't
     * exist. Is the equivalent of calling {@code getChannel(name, false);}
     *
     * @param name Then name of the channel
     * @return The channel
     */
    Channel getChannel(String name);

    /**
     * Gets a {@link Channel} of the given name and gives it the specified PM
     * status. Creates a new one if it doesn't exist. This allows a channel to
     * have the same name as a player without their tabs being merged.
     *
     * @param name The name of the channel
     * @param pm Whether it is for PMs or not
     * @return The channel
     */
    Channel getChannel(String name, boolean pm);

    /**
     * Adds a channel to be displayed on the tray.
     *
     * @param channel The channel to be added
     */
    void addChannel(Channel channel);

    /**
     * Removes a {@link Channel} from the tab list.
     *
     * @param channel The channel to remove
     */
    void removeChannel(Channel channel);

    /**
     * Gets an array of all {@link Channel}s currently displayed.
     *
     * @return An array of channels
     */
    Channel[] getChannels();

    /**
     * Gets the active {@link Channel}. The active channel is the channel whose
     * messages are shown.
     *
     * @return The active channel
     */
    Channel getActiveChannel();

    /**
     * Sets the active {@link Channel}.
     *
     * @param channel The new active channel
     */
    void setActiveChannel(Channel channel);

    /**
     * Clears all the messages in the chat and removes all channels.
     */
    void clearMessages();

    /**
     * Removes all messages with the given id.
     *
     * @param id The id
     */
    void removeMessages(int id);

}
