package mnm.mods.tabbychat.util;

public enum TimeStamps {

    MILITARY("[HHmm]", "[2359]", "\\[[0-9]{4}\\]"),
    MILITARYWITHCOLON("[HH:mm]", "[23:59]", "\\[[0-9]{2}:[0-9]{2}\\]"),
    STANDARD("[hh:mm]", "[12:00]", "\\[[0-9]{2}:[0-9]{2}\\]"),
    STANDARDWITHMARKER("[hh:mma]", "[12:00PM]", "\\[[0-9]{2}:[0-9]{2}(AM|PM)\\]"),
    MILITARYSECONDS("[HH:mm:ss]", "[23:59:01]", "\\[[0-9]{2}:[0-9]{2}:[0-9]{2}\\]"),
    STANDARDSECONDS("[hh:mm:ss]", "[12:00:01]", "\\[[0-9]{2}:[0-9]{2}:[0-9]{2}\\]"),
    STANDARDSECONDSMARKER("[hh:mm:ssa]", "[12:00:01PM]", "\\[[0-9]{2}:[0-9]{2}:[0-9]{2}(AM|PM)\\]");

    private String code;
    public String maxTime;
    public String regEx;

    private TimeStamps(String _code, String _maxTime, String _regex) {
        this.code = _code;
        this.maxTime = _maxTime;
        this.regEx = _regex;
    }

    public String toString() {
        return this.maxTime;
    }

    public String toCode() {
        return this.code;
    }
}
