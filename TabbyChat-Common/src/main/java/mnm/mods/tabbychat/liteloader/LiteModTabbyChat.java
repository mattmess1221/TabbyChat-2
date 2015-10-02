package mnm.mods.tabbychat.liteloader;

import java.io.File;
import java.net.SocketAddress;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.MnmUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.server.S01PacketJoinGame;

import com.mojang.realmsclient.dto.RealmsServer;
import com.mumfrey.liteloader.JoinGameListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.common.Resources;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.resources.ModResourcePack;
import com.mumfrey.liteloader.resources.ModResourcePackDir;

public class LiteModTabbyChat implements Tickable, JoinGameListener {

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
        loadUtils();
        tc.init();
    }

    @Override
    public void onTick(Minecraft arg0, float arg1, boolean arg2, boolean arg3) {
        tc.changeChatScreen(arg0.currentScreen);
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

    private void loadUtils() {
        File source = findClasspathRoot(MnmUtils.class);
        loadResourcePack(source, "Mnm Utils");
        try {
            Minecraft.class.getMethod("getMinecraft");
            // I'm in dev, fix things.
            loadResourcePack(findClasspathRoot(TabbyChat.class), "TabbyChat-Common");
        } catch (Exception e) {
            // unimportant
        }
    }

    private static File findClasspathRoot(Class<?> clas) {
        String str = clas.getProtectionDomain().getCodeSource().getLocation().toString();
        str = str.replace("/" + clas.getCanonicalName().replace('.', '/').concat(".class"), "");
        str = str.replace('\\', '/');
        if (str.endsWith("!")) {
            str = str.substring(0, str.length() - 1);
        }
        if (str.startsWith("jar:")) {
            str = str.substring(4);
        }
        if (str.startsWith("file:/")) {
            str = str.substring(6);
        }
        return new File(str);
    }

    private void loadResourcePack(File source, String name) {
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

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {
    }

}
