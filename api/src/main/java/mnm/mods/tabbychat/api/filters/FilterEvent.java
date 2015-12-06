package mnm.mods.tabbychat.api.filters;

import java.util.Set;
import java.util.regex.Matcher;

import com.google.common.collect.Sets;

import mnm.mods.tabbychat.api.Channel;
import net.minecraft.util.IChatComponent;

public class FilterEvent {

    public final Matcher matcher;
    public IChatComponent chat;
    public Set<Channel> channels = Sets.newHashSet();

    public FilterEvent(Matcher matcher, Set<Channel> channels, IChatComponent chat) {
        this.matcher = matcher;
        this.chat = chat;
        this.channels = channels;
    }
}
