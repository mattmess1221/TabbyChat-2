package mnm.mods.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.mumfrey.liteloader.LiteMod;

import java.util.Set;

public class MnmUtils {

    private Set<Class<? extends LiteMod>> disabledUpdates = Sets.newIdentityHashSet();
    private IChatProxy chatProxy = new DefaultChatProxy();

    public IChatProxy getChatProxy() {
        return chatProxy;
    }

    public void setChatProxy(IChatProxy chatProxy) {
        if (chatProxy == null) {
            chatProxy = new DefaultChatProxy();
        }
        this.chatProxy = chatProxy;
    }

    public Set<Class<? extends LiteMod>> getDisabledUpdates() {
        return ImmutableSet.copyOf(disabledUpdates);
    }

    public void disableUpdateCheck(Class<? extends LiteMod> mod) {
        this.disabledUpdates.add(mod);
    }

}
