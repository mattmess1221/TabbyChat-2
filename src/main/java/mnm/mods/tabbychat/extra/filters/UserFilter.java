package mnm.mods.tabbychat.extra.filters;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.RegEx;

public class UserFilter implements Filter {

    private String name = "New Filter";
    private FilterSettings settings = new FilterSettings();
    private String pattern = ".*";

    private transient Pattern expression;

    public void setPattern(@RegEx String pattern) throws PatternSyntaxException {
        testPatternUnsafe(pattern);
        this.pattern = pattern;
        this.expression = null;
    }

    public void testPattern(String pattern) throws UserPatternException {
        try {
            testPatternUnsafe(pattern);
        } catch (PatternSyntaxException e) {
            throw new UserPatternException(e);
        }
    }

    private void testPatternUnsafe(String pattern) throws PatternSyntaxException {
        Pattern.compile(resolvePattern(pattern));
    }

    @Override
    public Pattern getPattern() {
        if (expression == null) {
            this.expression = Pattern.compile(resolvePattern(pattern), settings.getFlags());
        }
        return expression;
    }

    private String resolvePattern(String pattern) {
        String resolved = resolveVariables(pattern);
        if (!settings.isRegex()) {
            resolved = Pattern.quote(resolved);
        }
        return resolved;
    }

    public String getRawPattern() {
        return this.pattern;
    }

    @RegEx
    private static String resolveVariables(String pattern) {
        Matcher matcher = Pattern.compile("\\$\\{([\\w\\d]+)}").matcher(pattern);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            String var = FilterAddon.getVariable(key);
            matcher.appendReplacement(buffer, var);
        }
        matcher.appendTail(buffer);

        return pattern;
    }

    @Override
    public void action(FilterEvent event) {

        if (settings.isRemove()) {
            // remove
            event.channels.clear();
        } else {
            // add channels
            for (String name : settings.getChannels()) {
                // replace group tokens in channel name
                Matcher matcher = Pattern.compile("\\$(\\d+)").matcher(name);
                while (matcher.find()) {
                    // find groups
                    int group = Integer.parseInt(matcher.group(1));
                    if (group > 0 && event.matcher.groupCount() >= group) {
                        String groupText = event.matcher.group(group);
                        if (groupText != null) {
                            name = name.replace(matcher.group(), groupText);
                            continue;
                        }
                    }
                    name = null;
                    break;
                }
                // skip this because there were missing or out of bounds groups.
                if (name == null)
                    continue;

                // PMs start with a '@' character
                boolean pm = name.startsWith("@");
                // remove # or @ from the start of the channel name
                if (name.startsWith("#") || name.startsWith("@"))
                    name = name.substring(1);

                Channel channel = TabbyAPI.getAPI().getChat().getChannel(name, pm);
                event.channels.add(channel);
            }
        }
        // play sound
        if (settings.isSoundNotification()) {
            settings.getSoundName()
                    .map(ResourceLocation::new)
                    .map(SoundEvent.REGISTRY::getObject)
                    .map(sndEvent -> PositionedSoundRecord.getMasterRecord(sndEvent, 1.0F))
                    .ifPresent(Minecraft.getMinecraft().getSoundHandler()::playSound);

        }
    }

    @Override
    public String prepareText(ITextComponent string) {
        return settings.isRaw() ? string.getFormattedText() : Filter.super.prepareText(string);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public FilterSettings getSettings() {
        return settings;
    }

    class UserPatternException extends Exception {
        private UserPatternException(PatternSyntaxException e) {
            super(e);
        }
    }
}
