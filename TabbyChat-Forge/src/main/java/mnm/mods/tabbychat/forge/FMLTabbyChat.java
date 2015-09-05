package mnm.mods.tabbychat.forge;

import java.io.File;
import java.net.SocketAddress;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.TabbyRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.InjectedModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

@Mod(modid = TabbyRef.MOD_ID, name = TabbyRef.MOD_NAME, version = TabbyRef.MOD_VERSION, clientSideOnly = true)
public class FMLTabbyChat extends TabbyChat {

    private File tempDir;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        tempDir = event.getModConfigurationDirectory();
        ForgeCommandsImpl.setInstance();
    }

    @Override
    protected void loadResourcePack(File file, final String name) {
        ModContainer thisCont = FMLCommonHandler.instance().findContainerFor(this);
        ModContainer container = new InjectedModContainer(thisCont, file) {
            @Override
            public String getName() {
                return name;
            }
        };

        FMLCommonHandler.instance().addModToResourcePack(container);

    }

    @EventHandler
    public void init(@SuppressWarnings("unused") FMLInitializationEvent event) {
        if (getAPI() == null) {
            setInstance(this);
            this.setConfigFolder(tempDir);
            FMLCommonHandler.instance().bus().register(this);
            init();
        } else {
            getLogger().info("TabbyChat already initialized. Not registering FML events.");
        }
    }

    @SubscribeEvent
    public void onRender(RenderTickEvent event) {
        if (event.phase == Phase.START) {
            GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
            onRender(currentScreen);
        }
    }

    @SubscribeEvent
    public void onJoin(ClientConnectedToServerEvent event) {
        if (event.isLocal) {
            onJoin((SocketAddress) null);
        } else {
            onJoin(event.manager.getRemoteAddress());
        }
    }
}
