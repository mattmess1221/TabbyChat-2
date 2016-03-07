package mnm.mods.tabbychat.extra.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import mnm.mods.tabbychat.api.filters.FilterVariable;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class ChatFilter implements Filter {

    private String name = "New Filter";
    private ChatFilterSettings settings = new ChatFilterSettings();
    private String pattern = "a^";
    private String action = DefaultAction.ID;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setPattern(String pattern) {
        try {
            Pattern.compile(resolveVariables(pattern));
            this.pattern = pattern;
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            TabbyAPI.getAPI().getChat().getChannel("TabbyChat")
                    .addMessage(new ChatComponentText(e.getMessage()));
        }
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile(resolveVariables(pattern));
    }

    @Override
    public String getUnresolvedPattern() {
        return pattern;
    }

    static String resolveVariables(String pattern) {
        Matcher matcher = Pattern.compile("\\$\\{([\\w\\d]+)\\}").matcher(pattern);
        while (matcher.find()) {
            String key = matcher.group(1);
            FilterVariable var = TabbyAPI.getAPI().getAddonManager().getFilterVariable(key);
            pattern = pattern.replace("${" + key + "}", var.getVar());
        }

        return pattern;
    }

    @Override
    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String getActionId() {
        return this.action;
    }

    @Override
    public IFilterAction getAction() {
        return TabbyAPI.getAPI().getAddonManager().getFilterAction(action);
    }

    @Override
    public FilterSettings getSettings() {
        return settings;
    }

    public void applyFilter(ChatReceivedEvent message) {
        String chat = StringUtils.stripControlCodes(message.chat.getUnformattedText());

        // Iterate through matches
        Pattern pattern = getPattern();
        if (pattern == null)
            return;
        Matcher matcher = pattern.matcher(chat);
        while (matcher.find()) {
            FilterEvent event = new FilterEvent(matcher, message.channels, message.chat);
            doAction(event);
            message.chat = event.chat; // Set the new chat
            message.channels = event.channels; // Add new channels.
        }
    }

    protected final void doAction(FilterEvent event) {
        IFilterAction action = getAction();
        if (action != null) {
            action.action(this, event);
        }
    }

    public static class DefaultAction implements IFilterAction {
        public static final String ID = "Default";

        @Override
        public void action(Filter filter, FilterEvent event) {
            FilterSettings settings = filter.getSettings();

            for (String name : settings.getChannels()) {
                // find groups
                Matcher matcher = Pattern.compile("^\\$(\\d+)$").matcher(name);
                if (matcher.find()) {
                    int group = Integer.parseInt(matcher.group(1));
                    if (group > 0 && event.matcher.groupCount() >= group) {
                        name = event.matcher.group(group);
                        if (name == null) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }

                boolean pm = settings.isDestinationPm();
                Channel channel = TabbyAPI.getAPI().getChat().getChannel(name, pm);
                event.channels.add(channel);
            }
            // remove
            if (settings.isRemove()) {
                event.channels.clear();
            }
            // play sound
            if (settings.isSoundNotification()) {
                String sname = settings.getSoundName();
                ResourceLocation loc = new ResourceLocation(sname);
                ISound sound = PositionedSoundRecord.create(loc);
                Minecraft.getMinecraft().getSoundHandler().playSound(sound);
            }
        }
    }

}
