package mnm.mods.tabbychat;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.net.SSendChannelMessage;
import mnm.mods.tabbychat.command.TCTellCommand;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.NetworkDirection;
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
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
        initNetwork();
    }

    @SubscribeEvent
    public void initClient(FMLClientSetupEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().register(new TabbyChatClient(dataFolder));
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        TCTellCommand.register(event.getCommandDispatcher());
    }

    private void initNetwork() {
        int id = 0;

        logger.info(TCMarkers.NETWORK, "Initializing network");

        channel.messageBuilder(SSendChannelMessage.class, id++)
                .encoder(SSendChannelMessage::encode)
                .decoder(SSendChannelMessage::new)
                .consumer(SSendChannelMessage::handle)
                .add();
    }

    public static void sendTo(ServerPlayerEntity player, String channel, ITextComponent text) {
        TabbyChat.channel.sendTo(new SSendChannelMessage(channel, text), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

}
