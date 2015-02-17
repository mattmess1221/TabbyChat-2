package mnm.mods.tabbychat.api.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.RegEx;

/**
 * A filter is used to filter chat. It has a {@link Pattern},
 * {@link IFilterAction}, and {@link FilterSettings}.
 */
public interface Filter {

    /**
     * Sets the name of this filter.
     *
     * @param name The new name
     */
    void setName(@Nonnull String name);

    /**
     * Sets the name of this filter.
     *
     * @return The name
     */
    @Nonnull
    String getName();

    /**
     * Sets the pattern that will trigger this filter.
     *
     * @param pattern The pattern as a String
     * @throws PatternSyntaxException If the pattern is not a regex
     */
    void setPattern(@RegEx @Nonnull String pattern) throws PatternSyntaxException;

    /**
     * Gets the pattern that will trigger this filter.
     *
     * @return The pattern
     */
    @Nullable
    Pattern getPattern();

    /**
     * Sets the action that this filter does when it matches.
     *
     * @param action The action id
     */
    void setAction(@Nonnull String action);

    /**
     * Gets the id of the action for this filter.
     *
     * @return The action id
     */
    @Nonnull
    String getActionId();

    /**
     * Gets the action that this filter does when it matches.
     *
     * @return The action or null if the id doesn't exist
     */
    @Nullable
    IFilterAction getAction();

    /**
     * Gets the {@link FilterSettings} this filter uses.
     *
     * @return The filter's settings
     */
    FilterSettings getSettings();

}