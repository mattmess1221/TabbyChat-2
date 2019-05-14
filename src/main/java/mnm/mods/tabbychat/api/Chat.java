package mnm.mods.tabbychat.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.util.text.ITextComponent;

import java.util.List;
import java.util.Set;

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
     * @param user The object representing the user
     * @return The channel
     */
    Channel getUserChannel(String user);

    /**
     * Gets an immutable set of all {@link Channel}s currently displayed.
     *
     * @return An array of channels
     */
    Set<? extends Channel> getChannels();

    List<? extends Message> getMessages(Channel channel);

    void addMessage(Channel channel, ITextComponent message);

    default void addMessage(Set<? extends Channel> channels, ITextComponent message) {
        for (Channel chan : channels) {
            addMessage(chan, message);
        }
    }

    default void addMessages(Channel channel, Iterable<ITextComponent> messages) {
        for (ITextComponent msg : messages) {
            addMessage(channel, msg);
        }
    }

    /**
     * Sends a message to every channel.
     *
     * @param message
     */
    default void broadcast(ITextComponent message) {
        addMessage(getChannels(), message);
    }
}
