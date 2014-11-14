package mnm.mods.tabbychat.api.listener;

import mnm.mods.tabbychat.api.listener.events.AddonInitEvent;

/**
 * Base interface for TabbyChat Modules
 *
 * @author Matthew Messinger
 *
 */
public interface TabbyAddon {

    /**
     * Gets called when the game loads. To not load this module, throw a
     * ModuleLoadError.
     */
    void initAddon(AddonInitEvent init);
}
