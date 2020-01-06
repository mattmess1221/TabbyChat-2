package mnm.mods.tabbychat.api

import net.minecraft.util.text.ITextComponent

/**
 * Represents the Chat.
 */
interface Chat {

    /**
     * Gets an immutable set of all [Channel]s currently displayed.
     *
     * @return An array of channels
     */
    val channels: Set<Channel>

    /**
     * Gets a [Channel] of the given type and name. Creates a new one if it doesn't
     * exist.
     *
     * @param type The type of the channel
     * @param name Then name of the channel
     * @return The channel
     */
    fun getChannel(type: ChannelType, name: String): Channel

    fun getMessages(channel: Channel): List<Message>

    fun addMessage(channel: Channel, message: ITextComponent)
}
