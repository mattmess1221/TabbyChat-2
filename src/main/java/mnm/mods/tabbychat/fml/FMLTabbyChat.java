package mnm.mods.tabbychat.fml;

import java.io.File;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.TweakTools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

@Mod(modid = TabbyRef.MOD_ID, name = TabbyRef.MOD_NAME, version = TabbyRef.MOD_VERSION)
public class FMLTabbyChat extends TabbyChat {

    private boolean shouldLoad;
    private File tempDir;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        shouldLoad = !TweakTools.isLiteLoaderLoaded();
        tempDir = event.getModConfigurationDirectory();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if (shouldLoad) {
            setInstance(this);
            this.setDataDirectory(tempDir);
            FMLCommonHandler.instance().bus().register(this);
            init();
        } else {
            getLogger().info("LiteLoader detected. Not registering FML events.");
        }
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent event) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        onRender(currentScreen);
    }

    @SubscribeEvent
    public void onJoin(ClientConnectedToServerEvent event) {
        onJoin(Minecraft.getMinecraft().getCurrentServerData());
    }
}
