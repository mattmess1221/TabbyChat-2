package mnm.mods.tabbychat.core.api;

import java.util.List;
import java.util.Map;

import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.api.listener.TabbyListener;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TabbyAddonManager implements AddonManager {

    private List<TabbyListener> addonsList = Lists.newArrayList();
    private Map<String, IFilterAction> filterActions = Maps.newHashMap();

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
            if (type.isAssignableFrom(chatBase.getClass())) {
                list.add((T) chatBase);
            }
        }
        return list;
    }

    @Override
    public void registerFilterAction(String name, IFilterAction action) {
        this.filterActions.put(name, action);
    }

    @Override
    public IFilterAction getFilterAction(String action) {
        return filterActions.get(action);
    }
}
