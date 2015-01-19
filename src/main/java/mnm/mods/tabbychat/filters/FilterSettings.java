package mnm.mods.tabbychat.filters;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.util.EnumChatFormatting;

/**
 * Defines the settings used by filters.
 */
public class FilterSettings {

    // destinations
    private final Set<String> channels = new HashSet<String>();
    private boolean remove = false;
    // highlighting
    private boolean highlight = false;
    private EnumChatFormatting color;
    private EnumChatFormatting format;
    // notifications
    private boolean soundNotification = false;
    private String soundName;

    public Set<String> getChannels() {
        return channels;
    }

    public boolean willRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public EnumChatFormatting getColor() {
        return color;
    }

    public void setColor(EnumChatFormatting color) {
        this.color = color;
    }

    public EnumChatFormatting getFormat() {
        return format;
    }

    public void setFormat(EnumChatFormatting format) {
        this.format = format;
    }

    public boolean isSoundNotification() {
        return soundNotification;
    }

    public void setSoundNotification(boolean sound) {
        this.soundNotification = sound;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }
}
