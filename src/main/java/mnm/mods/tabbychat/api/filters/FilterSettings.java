package mnm.mods.tabbychat.api.filters;

import org.apache.logging.log4j.core.helpers.Strings;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

/**
 * Defines the settings used by filters.
 */
public class FilterSettings {

    // destinations
    private final Set<String> channels = new HashSet<>();
    private boolean remove;
    private boolean clean = true;
    private boolean regex;
    private int flags;

    // notifications
    private boolean soundNotification = false;
    private String soundName = "";

    public Set<String> getChannels() {
        return channels;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean value) {
        this.remove = value;
    }

    public boolean isClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public boolean isCaseInsensitive() {
        return getFlag(Pattern.CASE_INSENSITIVE);
    }

    public void setCaseInsensitive(boolean value) {
        setFlag(Pattern.CASE_INSENSITIVE, value);
    }

    private void setFlag(int flag, boolean value) {
        if (value) {
            this.flags |= flag;
        } else {
            this.flags &= ~flag;
        }
    }

    private boolean getFlag(int flag) {
        return (flags & flag) != 0;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isSoundNotification() {
        return soundNotification;
    }

    public void setSoundNotification(boolean sound) {
        this.soundNotification = sound;
    }

    public Optional<String> getSoundName() {
        return Optional.ofNullable(soundName);
    }

    public void setSoundName(@Nullable String soundName) {
        this.soundName = Strings.isEmpty(soundName) ? null : soundName;
    }

}
