package mnm.mods.tabbychat.liteloader;

import java.io.File;
import java.net.SocketAddress;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S01PacketJoinGame;

public class LiteModTabbyChat implements JoinGameListener {

    private TabbyChat tc;

    @Override
    public String getName() {
        return TabbyRef.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return TabbyRef.MOD_VERSION;
    }

    @Override
    public void init(File configPath) {
        this.tc = new TabbyChat(configPath);
        tc.init();
    }

    @Override
    public void onJoinGame(INetHandler iNet, S01PacketJoinGame packet, ServerData serverData, RealmsServer realms) {
        NetHandlerPlayClient play = (NetHandlerPlayClient) iNet;
        SocketAddress addr = null;
        if (!play.getNetworkManager().isLocalChannel()) {
            addr = play.getNetworkManager().getRemoteAddress();
        }
        tc.onJoin(addr);
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

}
