package mnm.mods.tabbychat;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.net.SSendChannelMessage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(TabbyChat.MODID)
public class TabbyChat {

    public static final String MODID = "tabbychat";

    public static final Logger logger = LogManager.getLogger(MODID);

    public static final Path dataFolder = FMLPaths.CONFIGDIR.get().resolve(MODID);

    private static final String PROTOCL_VERSION = "1.0";

    public static final SimpleChannel channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(TabbyChat.MODID, "channel"))
            .networkProtocolVersion(() -> PROTOCL_VERSION)
            // TODO not required on either side
            .clientAcceptedVersions(PROTOCL_VERSION::equals)
            .serverAcceptedVersions(PROTOCL_VERSION::equals)
            .simpleChannel();

    public TabbyChat() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> TabbyChat::initClient);
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
        initNetwork();
    }

    private void initNetwork() {
        int id = 0;

        channel.messageBuilder(SSendChannelMessage.class, id++)
                .encoder(SSendChannelMessage::encode)
                .decoder(SSendChannelMessage::new)
                .consumer(SSendChannelMessage::handle)
                .add();
    }

    private static void initClient() {
        FMLJavaModLoadingContext.get().getModEventBus().register(TabbyChatClient.getInstance());
    }

}
