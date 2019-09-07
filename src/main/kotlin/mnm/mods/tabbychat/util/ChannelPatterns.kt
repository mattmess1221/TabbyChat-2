package mnm.mods.tabbychat.util

import java.util.regex.Pattern

enum class ChannelPatterns(
        private val translation: Translatable,
        pattern: String) : Translatable by translation {

    ANGLES(Translation.DELIMS_ANGLES, "^\\<%s\\>"),
    BRACES(Translation.DELIMS_BRACES, "^\\{%s\\}"),
    BRACKETS(Translation.DELIMS_BRACKETS, "^\\[%s\\]"),
    PARENS(Translation.DELIMS_PARENTHESIS, "^\\(%s\\)"),
    ANGLESPARENS(Translation.DELIMS_ANGLES_PARENS, "^<\\(%s\\) ?.{3,30}>"),
    ANGLESBRACKETS(Translation.DELIMS_ANGLES_BRAKETS, "^<\\[%s\\] ?.{3,30}>");

    val pattern: Pattern = Pattern.compile(String.format(pattern, "(.{1,16}?)"))

}
