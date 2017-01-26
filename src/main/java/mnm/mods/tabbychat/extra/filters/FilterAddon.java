package mnm.mods.tabbychat.extra.filters;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.api.filters.Filter;
import mnm.mods.tabbychat.api.filters.FilterEvent;
import mnm.mods.tabbychat.settings.ServerSettings;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterAddon {

    private static Map<String, Optional<Supplier<String>>> variables = Maps.newHashMap();

    private List<Filter> filters = Lists.newArrayList();

    public FilterAddon() {
        filters.add(new ChannelFilter());
        filters.add(new MessageFilter());

        variables.clear();

        Minecraft mc = Minecraft.getMinecraft();
        setVariable("player", () -> Pattern.quote(mc.player.getName()));
        setVariable("onlineplayer", () -> Joiner.on('|')
                .appendTo(new StringBuilder("(?:"), mc.world.playerEntities.stream()
                        .map(player -> Pattern.quote(player.getName()))
                        .iterator())
                .append(')').toString()
        );
    }

    private static void setVariable(String key, Supplier<String> supplier) {
        variables.put(key, Optional.of(supplier));
    }

    public static String getVariable(String key) {
        return variables.getOrDefault(key, Optional.empty())
                .map(Supplier::get)
                .orElse("");
    }

    @Subscribe
    public void onChatRecieved(ChatReceivedEvent message) {
        ServerSettings settings = TabbyChat.getInstance().serverSettings;
        if (settings == null) {
            // We're possibly not in game.
            return;
        }

        for (Filter filter : Iterables.concat(filters, settings.filters)) {
            Matcher matcher = filter.getPattern().matcher(filter.prepareText(message.text));
            while (matcher.find()) {
                FilterEvent event = new FilterEvent(matcher, message.channels, message.text);
                filter.action(event);
                message.text = event.text; // Set the new chat
                message.channels = event.channels; // Add new channels.
            }
        }
    }
}
