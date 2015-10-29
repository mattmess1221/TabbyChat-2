package mnm.mods.tabbychat.api.filters;

import java.util.Set;
import java.util.regex.Matcher;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.listener.events.Event;
import net.minecraft.util.IChatComponent;

import com.google.common.collect.Sets;

public class FilterEvent extends Event {

    public final Matcher matcher;
    public IChatComponent chat;
    public Set<Channel> channels = Sets.newHashSet();

    public FilterEvent(Matcher matcher, Set<Channel> channels, IChatComponent chat) {
        this.matcher = matcher;
        this.chat = chat;
        this.channels = channels;
    }
}
