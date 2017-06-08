package mnm.mods.util;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;
import mnm.mods.util.update.UpdateChecker;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.SPacketJoinGame;

import java.io.File;

public class LiteModMnmUtils implements JoinGameListener {

    private MnmUtils utils;

    @Override
    public String getName() {
        return "MnmUtils";
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init(File arg0) {
        this.utils = new MnmUtils();
    }

    @Override
    public void upgradeSettings(String arg0, File arg1, File arg2) {}

    @Override
    public void onJoinGame(INetHandler netHandler, SPacketJoinGame joinGamePacket, ServerData serverData, RealmsServer realmsServer) {
        UpdateChecker.runUpdateChecks(utils.getChatProxy(), utils.getDisabledUpdates());
    }

    public MnmUtils getUtils() {
        return utils;
    }

}
