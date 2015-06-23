package mnm.mods.tabbychat.extra.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.api.filters.FilterSettings;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class ChatFilter implements Filter {

    private String name = "New Filter";
    private ChatFilterSettings settings = new ChatFilterSettings();
    private Pattern pattern = Pattern.compile("a^");
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
            this.pattern = Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            TabbyAPI.getAPI().getChat().getChannel("TabbyChat")
                    .addMessage(new ChatComponentText(e.getMessage()));
        }
    }

    @Override
    public Pattern getPattern() {
        return this.pattern;
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

    public void applyFilter(ChatRecievedEvent message) {
        String chat = StringUtils.stripControlCodes(message.chat.getUnformattedText());

        // Iterate through matches
        Matcher matcher = getPattern().matcher(chat);
        while (matcher.find()) {
            FilterEvent event = new FilterEvent(matcher, message.channels, message.chat);
            doAction(event);
            message.chat = event.chat; // Set the new chat
            message.channels = event.channels; // Add new channels.
        }
    }

    protected final void doAction(FilterEvent event) {
        if (getAction() != null) {
            getAction().action(this, event);
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
                    if (group > 0 && event.matcher.groupCount() <= group) {
                        name = event.matcher.group(group);
                    } else {
                        break;
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
