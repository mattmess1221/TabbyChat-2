package mnm.mods.tabbychat.api.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.RegEx;

/**
 * A filter is used to filter chat. It has a {@link Pattern},
 * {@link IFilterAction}, and {@link FilterSettings}.
 */
public interface Filter {

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
    Pattern getPattern();

    /**
     * Sets the action that this filter does when it matches.
     *
     * @param action The action
     */
    void setAction(IFilterAction action);

    /**
     * Gets the action that this filter does when it matches.
     *
     * @return The action
     */
    IFilterAction getAction();

    /**
     * Gets the {@link FilterSettings} this filter uses.
     *
     * @return The filter's settings
     */
    FilterSettings getSettings();

}