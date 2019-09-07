package mnm.mods.tabbychat;

import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.command.TCTellCommand;
import mnm.mods.tabbychat.net.SNetworkVersion;
import mnm.mods.tabbychat.net.SSendChannelMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
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

    public static final String PROTOCOL_VERSION = "1";

    private static SimpleChannel channel = initNetwork();
    private static SimpleChannel versionChannel = initVersionNetwork();

    public TabbyChat() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void init(FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void initClient(FMLClientSetupEvent event) {
        FMLJavaModLoadingContext.get().getModEventBus().register(new TabbyChatClient(dataFolder));
    }

    @SubscribeEvent
    public void serverStarting(FMLServerStartingEvent event) {
        TCTellCommand.register(event.getCommandDispatcher());
    }

    @SubscribeEvent
    public void playerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        versionChannel.sendTo(new SNetworkVersion(PROTOCOL_VERSION), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    private static SimpleChannel initNetwork() {
        logger.info(TCMarkers.NETWORK, "Initializing network");

        // put the version in the name so clients without that version will ignore any packets
        ResourceLocation id = new ResourceLocation(MODID, "channel-v" + PROTOCOL_VERSION);
        SimpleChannel channel = newChannel(id, PROTOCOL_VERSION);

        channel.messageBuilder(SSendChannelMessage.class, 0)
                .encoder(SSendChannelMessage::encode)
                .decoder(SSendChannelMessage::new)
                .consumer(SSendChannelMessage::handle)
                .add();

        return channel;
    }

    private static SimpleChannel initVersionNetwork() {
        ResourceLocation id = new ResourceLocation(MODID, "version");
        SimpleChannel channel = newChannel(id, "1");

        channel.messageBuilder(SNetworkVersion.class, 0)
                .encoder(SNetworkVersion::encode)
                .decoder(SNetworkVersion::new)
                .consumer(SNetworkVersion::handle)
                .add();

        return channel;
    }

    private static SimpleChannel newChannel(ResourceLocation key, String version) {
        return NetworkRegistry.ChannelBuilder
                .named(key)
                .networkProtocolVersion(() -> version)
                // The network is optional, allow everyone
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
    }

    public static void sendTo(ServerPlayerEntity player, String channel, ITextComponent text) {
        TabbyChat.channel.sendTo(new SSendChannelMessage(channel, text), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

}
