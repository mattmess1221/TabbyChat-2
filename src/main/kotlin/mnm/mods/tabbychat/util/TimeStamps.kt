package mnm.mods.tabbychat.util

import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor

enum class TimeStamps(code: String, val display: String) {
    MILITARY("'['HHmm']'", "[2359]"),
    MILITARYWITHCOLON("'['HH:mm']'", "[23:59]"),
    STANDARD("'['hh':'mm']'", "[12:00]"),
    STANDARDWITHMARKER("'['hh':'mma']'", "[12:00PM]"),
    MILITARYSECONDS("'['HH':'mm':'ss']'", "[23:59:01]"),
    STANDARDSECONDS("'['hh':'mm':'ss']'", "[12:00:01]"),
    STANDARDSECONDSMARKER("'['hh':'mm':'ssa']'", "[12:00:01PM]");

    private val format: DateTimeFormatter = DateTimeFormatter.ofPattern(code)

    fun format(date: TemporalAccessor): String {
        return format.format(date)
    }

    override fun toString(): String {
        return display
    }
}
