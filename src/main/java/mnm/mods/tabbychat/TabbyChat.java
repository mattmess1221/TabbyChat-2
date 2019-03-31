package mnm.mods.tabbychat;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
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
        DistExecutor.runWhenOn(Dist.CLIENT, () -> TabbyChat::initclient);
    }

    private static void initclient() {
        FMLJavaModLoadingContext.get().getModEventBus().register(TabbyChatClient.getInstance());
    }

}
