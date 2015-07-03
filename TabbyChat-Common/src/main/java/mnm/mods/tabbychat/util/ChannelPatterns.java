package mnm.mods.tabbychat.util;

import java.util.regex.Pattern;

import mnm.mods.util.Translatable;

public enum ChannelPatterns {

    ANGLES(Translation.DELIMS_ANGLES, "\\<%s\\>"),
    BRACES(Translation.DELIMS_BRACES, "\\{%s\\}"),
    BRACKETS(Translation.DELIMS_BRACKETS, "\\[%s\\]"),
    PARENS(Translation.DELIMS_PARENTHESIS, "\\(%s\\)"),
    ANGLESPARENS(Translation.DELIMS_ANGLES_PARENS, "<\\(%s\\)(?: )?[\\w\\d]{3,30}>"),
    ANGLESBRACKETS(Translation.DELIMS_ANGLES_BRAKETS, "<\\[%s\\](?: )?[\\w\\d]{3,30}>");

    private final Translatable translation;
    private final Pattern pattern;

    private ChannelPatterns(Translatable title, String pattern) {
        this.translation = title;
        this.pattern = Pattern.compile(String.format(pattern, "([\\p{L}0-9_]{1,16})"));
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return translation.toString();
    }
}
