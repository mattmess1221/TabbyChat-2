package mnm.mods.tabbychat.core.api;

import mnm.mods.tabbychat.api.listener.TabbyAddon;
import mnm.mods.tabbychat.api.listener.events.PostLoginEvent;

/**
 * Here for internal use. It's suggested you use your loader's way.
 * {@link com.mumfrey.liteloader.PostLoginEvent} for LiteLoader
 * {@link cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent}
 * for FML
 *
 * @author Matthew
 */
public interface JoinGameListener extends TabbyAddon {

    void onJoinGame(PostLoginEvent loginEvent);
}
