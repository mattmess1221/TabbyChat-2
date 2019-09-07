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
     * Gets a [Channel] of the given name. Creates a new one if it doesn't
     * exist. Is the equivalent of calling `getChannel(name, false);`
     *
     * @param name Then name of the channel
     * @return The channel
     */
    fun getChannel(name: String): Channel

    /**
     * Gets a [Channel] of the given name and gives it the specified PM
     * status. Creates a new one if it doesn't exist. This allows a channel to
     * have the same name as a player without their tabs being merged.
     *
     * @param user The object representing the user
     * @return The channel
     */
    fun getUserChannel(user: String): Channel

    fun getMessages(channel: Channel): List<Message>

    fun addMessage(channel: Channel, message: ITextComponent)

    fun addMessage(channels: Set<Channel>, message: ITextComponent) {
        for (chan in channels) {
            addMessage(chan, message)
        }
    }

    fun addMessages(channel: Channel, messages: Iterable<ITextComponent>) {
        for (msg in messages) {
            addMessage(channel, msg)
        }
    }

    /**
     * Sends a message to every channel.
     *
     * @param message
     */
    fun broadcast(message: ITextComponent) {
        addMessage(channels, message)
    }
}
