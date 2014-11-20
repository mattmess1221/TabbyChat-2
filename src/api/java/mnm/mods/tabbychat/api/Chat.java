package mnm.mods.tabbychat.api;

import java.util.List;

import net.minecraft.util.IChatComponent;

/**
 * Represents the Chat.
 */
public interface Chat {

    /**
     * Gets a {@link Channel} of the given name. Creates a new one if it doesn't
     * exist.
     *
     * @param name Then name of the channel
     * @return The channel
     */
    Channel getChannel(String name);

    /**
     * Removes a {@link Channel} from the tab list.
     *
     * @param channel The channel to remove
     */
    void removeChannel(Channel channel);

    /**
     * Gets an array of all {@link Channel}s.
     *
     * @return An array of channels
     */
    Channel[] getChannels();

    /**
     * Gets a list of {@list Message}s.
     *
     * @return A list of messages
     */
    List<Message> getMessages();

    /**
     * Adds a message to the given channel
     *
     * @param channel The channel to send to
     * @param chat The message to send
     */
    void addMessage(Channel channel, IChatComponent chat);

    /**
     * Adds a message to the given channels.
     *
     * @param channels The channels
     * @param chat The message
     */
    void addMessage(Channel[] channels, IChatComponent chat);

    /**
     * Adds a message with the given ID to the given channels
     *
     * @param channel The channels
     * @param chat The message
     * @param id The ID
     */
    void addMessage(Channel[] channels, IChatComponent chat, int id);

    /**
     * Adds a message to the given channel with optional deletion. Any messages
     * previously added with the same id will be removed.
     *
     * @param channel The channel to send to
     * @param chat The message to send
     * @param id The id of the message
     */
    void addMessage(Channel channel, IChatComponent chat, int id);

    /**
     * Removes messages at the given position.
     *
     * @param pos The position
     */
    void removeMessageAt(int pos);

    /**
     * Removes all messages with the given id.
     *
     * @param id The id
     */
    void removeMessages(int id);

    /**
     * Clears all the messages in the chat.
     */
    void clearMessages();

}
