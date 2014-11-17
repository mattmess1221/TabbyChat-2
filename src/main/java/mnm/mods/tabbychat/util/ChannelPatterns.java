package mnm.mods.tabbychat.util;

import net.minecraft.client.resources.I18n;

public enum ChannelPatterns {

    ANGLES("tabbychat.delims.angles", "<", ">"),
    BRACES("tabbychat.delims.braces", "{", "}"),
    BRACKETS("tabbychat.delims.brackets", "[", "]"),
    PARENS("tabbychat.delims.parenthesis", "(", ")"),
    ANGLESPARENS("tabbychat.delims.anglesparens", "<(", ")(?: )?[A-Za-z0-9_]{1-16}>"),
    ANGLESBRACKETS("tabbychat.delims.anglesbrackets", "<\\[", "](?: )?[A-Za-z0-9_]{1-16}>");

    private final String unlocalized;
    private final String open;
    private final String close;

    private ChannelPatterns(String title, String open, String close) {
        this.unlocalized = title;
        this.open = open;
        this.close = close;
    }

    public String getTitle() {
        return unlocalized;
    }

    public String getLocalizedName() {
        return I18n.format(getTitle());
    }

    public String getOpen() {
        return open;
    }

    public String getClose() {
        return close;
    }
}
