package mnm.mods.tabbychat.api;

import net.minecraft.util.text.ITextComponent;

import java.util.List;
import javax.annotation.Nullable;

/**
 * Represents a channel.
 */
public interface Channel {

    /**
     * Gets the name of this channel.
     *
     * @return The name
     */
    String getName();

    /**
     * Gets whether this channel is used for a personal message.
     *
     * @return PM status
     */
    boolean isPm();

    /**
     * Gets the alias that is displayed on the channel tab.
     *
     * @return The alias
     */
    String getAlias();

    /**
     * Sets the alias that is displayed on the channel tab.
     *
     * @param alias The new alias
     */
    void setAlias(String alias);

    /**
     * Gets the prefix that is inserted before the input.
     *
     * @return The prefix
     */
    String getPrefix();

    /**
     * Sets the prefix that is inserted before the input.
     *
     * @param prefix The new prefix
     */
    void setPrefix(String prefix);

    /**
     * Gets whether the prefix is hidden or not. A hidden prefix will not be
     * displayed while typing. Defaults to false.
     *
     * @return True if the prefix is hidden
     */
    boolean isPrefixHidden();

    /**
     * Sets whether the prefix is hidden or not. A hidden prefix will not be
     * displayed while typing.
     *
     * @param hidden Whether to hide the prefix
     */
    void setPrefixHidden(boolean hidden);

    String getCommand();

    void setCommand(String cmd);

    /**
     * Gets the channel's current status. A null indicates that there is no
     * status.
     *
     * @return The status or null
     */
    @Nullable
    ChannelStatus getStatus();

    /**
     * Sets the channel's current status.
     *
     * @param status The new status
     */
    void setStatus(@Nullable ChannelStatus status);

    /**
     * Opens the settings panel for this channel.
     */
    void openSettings();

    /**
     * Gets a list of {@link Message}s in this {@link Channel}.
     *
     * @return A list of messages
     */
    List<Message> getMessages();

    /**
     * Adds a message to the channel.
     *
     * @param text The message
     */
    void addMessage(ITextComponent text);

    /**
     * Adds a message to the channel with optional deletion. Any messages
     * previously added with the same id will be removed.
     *
     * @param text The message to send
     * @param id The id of the message
     */
    void addMessage(ITextComponent text, int id);

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
     * Clears this channel of all message.
     */
    void clear();

}
