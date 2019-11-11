package mnm.mods.tabbychat

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.command.TCTellCommand
import mnm.mods.tabbychat.net.SNetworkVersion
import mnm.mods.tabbychat.net.SSendChannelMessage
import net.alexwells.kottle.FMLKotlinModLoadingContext
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent
import net.minecraftforge.fml.loading.FMLPaths
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.simple.SimpleChannel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.nio.file.Path

@Mod(MODID)
object TabbyChat {
    val logger: Logger = LogManager.getLogger(MODID)

    val dataFolder: Path = FMLPaths.CONFIGDIR.get().resolve(MODID)

    private val channel = initNetwork()
    private val versionChannel = initVersionNetwork()

    init {
        MinecraftForge.EVENT_BUS.register(this)
        FMLKotlinModLoadingContext.get().modEventBus.register(this)
    }

    @SubscribeEvent
    fun initClient(event: FMLClientSetupEvent) {
        logger.info("client setup")
        MinecraftForge.EVENT_BUS.register(TabbyChatClient)
        FMLKotlinModLoadingContext.get().modEventBus.register(TabbyChatClient)
    }

    @SubscribeEvent
    fun serverStarting(event: FMLServerStartingEvent) {
        TCTellCommand.register(event.commandDispatcher)
    }

    @SubscribeEvent
    fun playerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.player as ServerPlayerEntity
        versionChannel.sendTo(SNetworkVersion(PROTOCOL_VERSION), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT)
    }

    private fun initNetwork(): SimpleChannel {
        logger.info(NETWORK, "Initializing network")

        // put the version in the name so clients without that version will ignore any packets
        val id = ResourceLocation(MODID, "channel-v$PROTOCOL_VERSION")
        val channel = newChannel(id, PROTOCOL_VERSION)

        channel.messageBuilder(SSendChannelMessage::class.java, 0)
                .encoder(SSendChannelMessage::encode)
                .decoder(::SSendChannelMessage)
                .consumer(SSendChannelMessage::handle)
                .add()

        return channel
    }

    private fun initVersionNetwork(): SimpleChannel {
        val id = ResourceLocation(MODID, "version")
        val channel = newChannel(id, "1")

        channel.messageBuilder(SNetworkVersion::class.java, 0)
                .encoder(SNetworkVersion::encode)
                .decoder(::SNetworkVersion)
                .consumer(SNetworkVersion::handle)
                .add()

        return channel
    }

    private fun newChannel(key: ResourceLocation, version: String): SimpleChannel {
        return NetworkRegistry.ChannelBuilder
                .named(key)
                .networkProtocolVersion { version }
                // The network is optional, allow everyone
                .clientAcceptedVersions { true }
                .serverAcceptedVersions { true }
                .simpleChannel()
    }

    fun sendTo(player: ServerPlayerEntity, channel: String, text: ITextComponent) {
        this.channel.sendTo(SSendChannelMessage(channel, text), player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT)
    }
}
