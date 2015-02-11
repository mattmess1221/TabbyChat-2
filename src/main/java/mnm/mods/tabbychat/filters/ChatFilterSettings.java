package mnm.mods.tabbychat.filters;

import java.util.HashSet;
import java.util.Set;

import mnm.mods.tabbychat.api.filters.FilterSettings;
import net.minecraft.util.EnumChatFormatting;

/**
 * Defines the settings used by filters.
 */
public class ChatFilterSettings implements FilterSettings {

    // destinations
    private final Set<String> channels = new HashSet<String>();
    // highlighting
    private boolean highlight = false;
    private EnumChatFormatting color;
    private EnumChatFormatting format;
    // notifications
    private boolean soundNotification = false;
    private String soundName = "";

    @Override
    public Set<String> getChannels() {
        return channels;
    }

    @Override
    public boolean isHighlight() {
        return highlight;
    }

    @Override
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public EnumChatFormatting getColor() {
        return color;
    }

    @Override
    public void setColor(EnumChatFormatting color) {
        if (color != null && !color.isColor()) {
            throw new IllegalArgumentException(color.getFriendlyName() + " is not a color.");
        }
        this.color = color;
    }

    @Override
    public EnumChatFormatting getFormat() {
        return format;
    }

    @Override
    public void setFormat(EnumChatFormatting format) {
        if (format != null && !format.isFancyStyling()) {
            throw new IllegalArgumentException(format.getFriendlyName() + " is not formatting.");
        }
        this.format = format;
    }

    @Override
    public boolean isSoundNotification() {
        return soundNotification;
    }

    @Override
    public void setSoundNotification(boolean sound) {
        this.soundNotification = sound;
    }

    @Override
    public String getSoundName() {
        return soundName;
    }

    @Override
    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }
}
