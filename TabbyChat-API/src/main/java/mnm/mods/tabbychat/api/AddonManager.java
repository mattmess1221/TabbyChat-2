package mnm.mods.tabbychat.api;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mnm.mods.tabbychat.api.filters.IFilterAction;
import mnm.mods.tabbychat.api.listener.TabbyListener;

/**
 * Handles registering various types with TabbyChat.
 *
 * @author Matthew Messinger
 */
public interface AddonManager {

    /**
     * Registers a listener for an addon so it can recieve events.
     *
     * @param listener The listener
     */
    void registerListener(TabbyListener listener);

    /**
     * Gets a list of listeners of the specified type. May be empty if there are
     * none registered.
     *
     * @param type The class type of the listener
     * @return A list of registered listeners
     */
    @Nonnull
    <T extends TabbyListener> List<T> getListenersOfType(Class<T> type);

    /**
     * Registers a filter action name so it can be used and serialized.
     * Registering a name that is already registered will overwrite the previous
     * one.
     *
     * @param name The name to register for the action
     * @param action The action
     */
    void registerFilterAction(String name, @Nonnull IFilterAction action);

    /**
     * Gets a filter by name. Will return null if it is not registered.
     *
     * @param action The registered action id
     * @return The action or null
     */
    @Nullable
    IFilterAction getFilterAction(String action);

}
