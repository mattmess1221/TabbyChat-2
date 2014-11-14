package mnm.mods.tabbychat.liteloader;

import java.io.File;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S01PacketJoinGame;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.RenderListener;

public class LiteModTabbyChat extends TabbyChat implements RenderListener, JoinGameListener {

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
        setInstance(this);
        setDataDirectory(configPath);
        init();
    }

    @Override
    public void onRenderGui(GuiScreen currentScreen) {
        onRender(currentScreen);
    }

    @Override
    public void onJoinGame(INetHandler iNet, S01PacketJoinGame packet, ServerData serverData,
            RealmsServer realms) {
        onJoin(serverData);
    }

    // Unused
    // | | |
    // V V V
    @Override
    public void onRender() {
    }

    @Override
    public void onRenderWorld() {
    }

    @Override
    public void onSetupCameraTransform() {
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }

}
