package mnm.mods.tabbychat.api.filters;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumChatFormatting;

/**
 * Settings used for filters. Contains settings such as what channels they are
 * in, how they are formatted, and how notifications are handled.
 */
public interface FilterSettings {

    /**
     * Gets the {@link Set} of channels the message will be sent to. Channels
     * are represented by their String id.
     *
     * @return A set of channels
     */
    @Nonnull
    Set<String> getChannels();

    /**
     * Gets whether or not to remove the match from chat.
     *
     * @return True if should remove
     */
    boolean isRemove();

    /**
     * Sets whether or not to remove the match from chat.
     *
     * @param value new value
     */
    void setRemove(boolean value);

    /**
     * Returns true if the destination channel should be a PM.
     *
     * @return pm
     */
    boolean isDestinationPm();

    /**
     * Sets whether the destination channel should be a PM.
     *
     * @param isDestinationPm pm
     */
    void setDestinationPm(boolean isDestinationPm);

    /**
     * Returns whether highlighting should be enabled for this filter.
     *
     * @return True if highlighting is enabled
     */
    boolean isHighlight();

    /**
     * Sets whether highlighting should be enabled for this filter.
     *
     * @param highlight The new value
     */
    void setHighlight(boolean highlight);

    /**
     * Gets the color used for highlighting.
     *
     * @return The color used for highlighting
     */
    @Nullable
    EnumChatFormatting getColor();

    /**
     * Sets the color used for highlighting. Only use colors.
     *
     * @param color The color of the highlight.
     * @throws IllegalArgumentException if the color is not a color
     */
    void setColor(@Nullable EnumChatFormatting color);

    /**
     * Gets the formatting used for highlighting.
     *
     * @return The formatting used for highlighting
     */
    @Nullable
    EnumChatFormatting getFormat();

    /**
     * Sets the formatting for highlighting. Only use formatting.
     *
     * @param format The format of the highlight.
     * @throws IllegalArgumentException if the format is not a format.
     */
    void setFormat(@Nullable EnumChatFormatting format);

    /**
     * Gets if this filter has a sound notification.
     *
     * @return True if there is a sound notification.
     */
    boolean isSoundNotification();

    /**
     * Sets whether to use a sound notification.
     *
     * @param sound The sound's name
     */
    void setSoundNotification(boolean sound);

    /**
     * Gets the name of the sound to play.
     *
     * @return The sound's name
     */
    @Nonnull
    String getSoundName();

    /**
     * Sets the sound name for the sound notification. For example, to use an
     * orb sound, use {@code random.orb} and to use the anvil land sound,
     * {@code random.anvil_land}.
     *
     * @param soundName The new sound notification's name.
     */
    void setSoundName(@Nonnull String soundName);

}