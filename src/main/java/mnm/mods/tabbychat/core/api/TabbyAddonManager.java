package mnm.mods.tabbychat.core.api;

import com.google.common.collect.Maps;
import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.filters.FilterVariable;
import mnm.mods.tabbychat.api.filters.FilterVariable.FilterConstant;
import mnm.mods.tabbychat.api.filters.IFilterAction;

import java.util.Map;
import javax.annotation.Nonnull;

public class TabbyAddonManager implements AddonManager {

    private Map<String, IFilterAction> filterActions = Maps.newHashMap();
    private Map<String, FilterVariable> filterVariables = Maps.newHashMap();


    @Override
    public void registerFilterAction(String name, @Nonnull IFilterAction action) {
        this.filterActions.put(name, action);
    }

    @Override
    public IFilterAction getFilterAction(String action) {
        return filterActions.get(action);
    }

    @Override
    public FilterVariable getFilterVariable(String key) {
        if (!filterVariables.containsKey(key)) {
            return FilterVariable.NULL;
        }
        return filterVariables.get(key);

    }

    @Override
    public void setFilterVariable(String var, FilterVariable val) {
        this.filterVariables.put(var, val);
    }

    @Override
    public void setFilterConstant(String var, final String val) {
        setFilterVariable(var, new FilterConstant(val));
    }
}
