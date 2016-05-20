package mnm.mods.tabbychat.api.filters;

/**
 * Used to create actions for a {@link Filter}. Implement and invoke
 * {@link Filter#setAction(String)} to use.
 */
public interface IFilterAction {

    /**
     * The action that the {@link Filter} triggers.
     *
     * @param filter The filter that triggered this action
     * @param event The filter event
     */
    void action(Filter filter, FilterEvent event);
}
