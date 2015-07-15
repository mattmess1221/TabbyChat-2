package mnm.mods.tabbychat.liteloader;

import java.io.File;
import java.net.SocketAddress;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.RenderListener;
import com.mumfrey.liteloader.common.Resources;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.resources.ModResourcePack;
import com.mumfrey.liteloader.resources.ModResourcePackDir;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S01PacketJoinGame;

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
        setConfigFolder(configPath);
        init();
    }

    @Override
    public void onRenderGui(GuiScreen currentScreen) {
        onRender(currentScreen);

        // workaround for lack of disconnect interface
        if (serverSettings != null) {
            // test for disconnect
            Class<?>[] screens = { GuiDisconnected.class, GuiMainMenu.class };
            for (Class<?> c : screens) {
                if (c.isInstance(currentScreen)) {
                    onDisconnect();
                    return;
                }
            }

        }
    }

    @Override
    public void onJoinGame(INetHandler iNet, S01PacketJoinGame packet, ServerData serverData,
            RealmsServer realms) {
        NetHandlerPlayClient play = (NetHandlerPlayClient) iNet;
        SocketAddress addr;
        if (play.getNetworkManager().isLocalChannel()) {
            addr = null;
        } else {
            addr = play.getNetworkManager().getRemoteAddress();
        }
        onJoin(addr);
    }

    @Override
    protected void loadResourcePack(File source, String name) {
        IResourcePack pack;
        if (source.isFile()) {
            pack = new ModResourcePack(name, source);
        } else if (source.isDirectory()) {
            pack = new ModResourcePackDir(name, source);
        } else {
            // probably doesn't exist :(
            return;
        }
        @SuppressWarnings("unchecked")
        Resources<IResourceManager, IResourcePack> resources = (Resources<IResourceManager, IResourcePack>) LiteLoader
                .getGameEngine().getResources();
        resources.registerResourcePack(pack);
    }

    // Unused
    // | | |
    // V V V
    @Override
    public void onRender() {}

    @Override
    public void onRenderWorld() {}

    @Override
    public void onSetupCameraTransform() {}

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

}
