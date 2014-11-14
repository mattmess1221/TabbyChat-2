package mnm.mods.tabbychat.api.listener.events;

import net.minecraft.client.multiplayer.ServerData;

public class PostLoginEvent extends Event {

    public ServerData serverData;

    public PostLoginEvent(ServerData server) {
        this.serverData = server;
    }
}
