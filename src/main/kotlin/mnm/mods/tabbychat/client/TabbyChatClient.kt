package mnm.mods.tabbychat.client

import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.STARTUP
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.gui.GuiNewChatTC
import mnm.mods.tabbychat.client.settings.ServerSettings
import mnm.mods.tabbychat.client.settings.TabbySettings
import mnm.mods.tabbychat.util.ChatTextUtils
import mnm.mods.tabbychat.util.config.ConfigManager
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.IngameGui
import net.minecraft.client.gui.NewChatGui
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod.EventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
object TabbyChatClient {

    val settings: TabbySettings = TabbySettings(TabbyChat.dataFolder)

    lateinit var serverSettings: ServerSettings

    init {
        ConfigManager.addConfigs(settings)
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    private fun removeChannelTags(event: MessageAddedToChannelEvent.Pre) {
        if (settings.advanced.hideTag && event.channel !== DefaultChannel) {
            val pattern = serverSettings.general.channelPattern

            val text = event.text
            if (text != null) {
                val matcher = pattern.pattern.matcher(text.string)
                if (matcher.find()) {
                    event.text = ChatTextUtils.subChat(text, matcher.end())
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
    private object StartListener {
        var IngameGui.chat: NewChatGui
            get() = this.chatGUI
            set(chat) {
                try {
                    ObfuscationReflectionHelper.setPrivateValue(IngameGui::class.java, this, chat, "field_73840_e")
                    TabbyChat.logger.info(STARTUP, "Successfully hooked into chat.")
                } catch (e: Throwable) {
                    TabbyChat.logger.fatal(STARTUP, "Unable to hook into chat. This is bad.", e)
                }
            }

        @SubscribeEvent
        fun onFirstTick(event: TickEvent.ClientTickEvent) {
            // Do the first tick, then unregister self.
            // essentially an on-thread startup complete listener
            mc.ingameGUI.chat = GuiNewChatTC

            // unregister self so it doesn't get called again.
            FORGE_BUS.unregister(StartListener)
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    private fun onClientLogin(event: ClientPlayerNetworkEvent.LoggedInEvent) {
        // load chat
        try {
            serverSettings = ServerSettings(TabbyChat.dataFolder, event.networkManager!!.remoteAddress)
            ChatManager.loadFrom(serverSettings.config.nioPath.parent)
        } catch (e: Exception) {
            TabbyChat.logger.warn(CHATBOX, "Unable to load chat data.", e)
        }
    }
}
