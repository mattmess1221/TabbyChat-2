package mnm.mods.tabbychat.api.filters;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Nonnull;
import javax.annotation.RegEx;

import mnm.mods.tabbychat.filters.FilterSettings;

public interface Filter {

    void setPattern(@RegEx @Nonnull String pattern) throws PatternSyntaxException;

    Pattern getPattern();

    void setAction(IFilterAction action);

    IFilterAction getAction();

    FilterSettings getSettings();

}