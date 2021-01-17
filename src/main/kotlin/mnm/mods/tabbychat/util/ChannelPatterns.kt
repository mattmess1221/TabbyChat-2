package mnm.mods.tabbychat.util

enum class ChannelPatterns(
        val display: Translatable,
        pattern: String) {

    ANGLES(Translation.DELIMS_ANGLES, "^\\<%s\\>"),
    BRACES(Translation.DELIMS_BRACES, "^\\{%s\\}"),
    BRACKETS(Translation.DELIMS_BRACKETS, "^\\[%s\\]"),
    PARENS(Translation.DELIMS_PARENTHESIS, "^\\(%s\\)"),
    ANGLESPARENS(Translation.DELIMS_ANGLES_PARENS, "^<\\(%s\\) ?.{3,30}>"),
    ANGLESBRACKETS(Translation.DELIMS_ANGLES_BRAKETS, "^<\\[%s\\] ?.{3,30}>");

    val pattern = Regex(String.format(pattern, "(.{1,16}?)"))

}
