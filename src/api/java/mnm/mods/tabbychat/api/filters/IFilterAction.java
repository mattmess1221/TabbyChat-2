package mnm.mods.tabbychat.api.filters;

import mnm.mods.tabbychat.filters.FilterEvent;

public interface IFilterAction {

    void action(Filter filter, FilterEvent event);
}
