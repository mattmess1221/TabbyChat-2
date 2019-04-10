package mnm.mods.tabbychat;

import mnm.mods.tabbychat.client.TabbyChatClient;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(TabbyChat.MODID)
public class TabbyChat {

    public static final String MODID = "tabbychat";

    public static final Logger logger = LogManager.getLogger(MODID);

    public static final Path dataFolder = FMLPaths.CONFIGDIR.get().resolve(MODID);

    public TabbyChat() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void initClient(FMLClientSetupEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().register(new TabbyChatClient(dataFolder));
    }

}
