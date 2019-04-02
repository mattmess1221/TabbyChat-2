package mnm.mods.tabbychat.util;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public enum TimeStamps implements Translatable {

    MILITARY("'['HHmm']'", "[2359]"),
    MILITARYWITHCOLON("'['HH:mm']'", "[23:59]"),
    STANDARD("'['hh':'mm']'", "[12:00]"),
    STANDARDWITHMARKER("'['hh':'mma']'", "[12:00PM]"),
    MILITARYSECONDS("'['HH':'mm':'ss']'", "[23:59:01]"),
    STANDARDSECONDS("'['hh':'mm':'ss']'", "[12:00:01]"),
    STANDARDSECONDSMARKER("'['hh':'mm':'ssa']'", "[12:00:01PM]");

    private String code;
    private String maxTime;
    private DateTimeFormatter format;

    TimeStamps(String _code, String _maxTime) {
        this.code = _code;
        this.maxTime = _maxTime;
        format = DateTimeFormatter.ofPattern(code);
    }

    public String format(TemporalAccessor date) {
        return format.format(date);
    }

    @Override
    public String getUnlocalized() {
        return this.maxTime;
    }

    @Override
    public String translate(Object... params) {
        return this.maxTime;
    }

    public String toCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return translate();
    }

}
