package mnm.mods.tabbychat.util;

import mnm.mods.util.Translatable;

public enum ChannelPatterns implements Translatable {

    ANGLES(Translation.DELIMS_ANGLES, "<", ">"),
    BRACES(Translation.DELIMS_BRACES, "{", "}"),
    BRACKETS(Translation.DELIMS_BRACKETS, "[", "]"),
    PARENS(Translation.DELIMS_PARENTHESIS, "(", ")"),
    ANGLESPARENS(Translation.DELIMS_ANGLES_PARENS, "<(", ")(?: )?[A-Za-z0-9_]{1-16}>"),
    ANGLESBRACKETS(Translation.DELIMS_ANGLES_BRAKETS, "<\\[", "](?: )?[A-Za-z0-9_]{1-16}>");

    private final Translatable translation;
    private final String open;
    private final String close;

    private ChannelPatterns(Translatable title, String open, String close) {
        this.translation = title;
        this.open = open;
        this.close = close;
    }


    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }

    @Override
    public String getUnlocalized() {
        return translation.getUnlocalized();
    }

    @Override
    public String translate(Object... params) {
        return translation.translate(params);
    }
}
