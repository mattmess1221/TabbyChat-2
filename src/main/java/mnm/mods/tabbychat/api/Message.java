package mnm.mods.tabbychat.api;

import java.util.Date;

import net.minecraft.util.text.ITextComponent;

/**
 * Represents a message.
 */
public interface Message {

    /**
     * Gets the message
     *
     * @return The message
     */
    ITextComponent getMessage();

    /**
     * Gets the message with a time stamp if they are enabled.
     *
     * @return The time-stamped message
     */
    @Deprecated // impl detail
    ITextComponent getMessageWithOptionalTimestamp();

    /**
     * Gets the update counter used for this message.
     *
     * @return The counter
     */
    @Deprecated // impl detail
    int getCounter();

    /**
     * Gets the ID of this message. 0 is a normal message.
     *
     * @return The ID of this message
     */
    @Deprecated // impl detail
    int getID();

    /**
     * Gets the date that this message was sent.
     *
     * @return The date
     */
    Date getDate();


}
