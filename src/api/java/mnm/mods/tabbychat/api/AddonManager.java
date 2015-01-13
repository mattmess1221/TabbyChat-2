package mnm.mods.tabbychat.api;

import java.util.List;

import mnm.mods.tabbychat.api.listener.TabbyListener;

public interface AddonManager {

    void registerListener(TabbyListener listener);

    <T extends TabbyListener> List<T> getListenersOfType(Class<T> type);
}
