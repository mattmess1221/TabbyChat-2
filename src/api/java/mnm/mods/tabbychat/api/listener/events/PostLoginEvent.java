package mnm.mods.tabbychat.api.listener.events;

import java.net.SocketAddress;

public class PostLoginEvent extends Event {

    /**
     * The ip of the server being connected to. Null means single player.
     */
    public SocketAddress serverData;

    public PostLoginEvent(SocketAddress address) {
        this.serverData = address;
    }
}
