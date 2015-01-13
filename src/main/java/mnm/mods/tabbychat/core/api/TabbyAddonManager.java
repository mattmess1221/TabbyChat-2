package mnm.mods.tabbychat.core.api;

import java.util.List;

import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.listener.TabbyListener;

import com.google.common.collect.Lists;

public class TabbyAddonManager implements AddonManager {

    private List<TabbyListener> addonsList = Lists.newArrayList();

    @Override
    public void registerListener(TabbyListener chatBase) {
        if (!this.addonsList.contains(chatBase)) {
            this.addonsList.add(chatBase);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends TabbyListener> List<T> getListenersOfType(Class<T> type) {
        List<T> list = Lists.newArrayList();
        for (TabbyListener chatBase : this.addonsList) {
            if (type.isAssignableFrom(chatBase.getClass()))
                list.add((T) chatBase);
        }
        return list;
    }
}
