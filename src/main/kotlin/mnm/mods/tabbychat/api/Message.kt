package mnm.mods.tabbychat.api

import java.time.LocalDateTime

import net.minecraft.util.text.ITextComponent

/**
 * Represents a message.
 */
interface Message {

    /**
     * Gets the message
     *
     * @return The message
     */
    val message: ITextComponent

    /**
     * Gets the date that this message was sent.
     *
     * @return The date
     */
    val dateTime: LocalDateTime?

}
