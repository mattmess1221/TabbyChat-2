package mnm.mods.tabbychat.client

import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.STARTUP
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.core.GuiNewChatTC
import mnm.mods.tabbychat.client.extra.ChatAddonAntiSpam
import mnm.mods.tabbychat.client.extra.ChatLogging
import mnm.mods.tabbychat.client.extra.filters.FilterAddon
import mnm.mods.tabbychat.client.extra.spell.Spellcheck
import mnm.mods.tabbychat.client.settings.ServerSettings
import mnm.mods.tabbychat.client.settings.TabbySettings
import mnm.mods.tabbychat.util.ChatTextUtils
import mnm.mods.tabbychat.util.listen
import mnm.mods.tabbychat.util.mc
import net.alexwells.kottle.KotlinEventBusSubscriber
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.IngameGui
import net.minecraft.client.gui.NewChatGui
import net.minecraft.resources.IReloadableResourceManager
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.ObfuscationReflectionHelper
import net.minecraftforge.fml.loading.FMLPaths

object TabbyChatClient {
    val spellcheck = Spellcheck(TabbyChat.dataFolder)


    val settings: TabbySettings = TabbySettings(TabbyChat.dataFolder).apply {
        TabbyChat.logger.info("Loading TabbyChat settings")
        load()
    }
    var serverSettings: ServerSettings? = null

    init {
        // Keeps the current language updated whenever it is changed.
        val irrm = mc.resourceManager as IReloadableResourceManager
        irrm.addReloadListener(spellcheck)

        MinecraftForge.EVENT_BUS.listen<MessageAddedToChannelEvent.Pre>(EventPriority.LOW) {
            removeChannelTags(it)
        }
        MinecraftForge.EVENT_BUS.register(ChatAddonAntiSpam)
        MinecraftForge.EVENT_BUS.register(FilterAddon)
        MinecraftForge.EVENT_BUS.register(ChatLogging(FMLPaths.GAMEDIR.get().resolve("logs/chat")))
    }

    private fun removeChannelTags(event: MessageAddedToChannelEvent.Pre) {
        if (settings.advanced.hideTag.value && event.channel !== DefaultChannel) {
            val pattern = serverSettings!!.general.channelPattern.value

            val text = event.text
            if (text != null) {
                val matcher = pattern.pattern.matcher(text.string)
                if (matcher.find()) {
                    event.text = ChatTextUtils.subChat(text, matcher.end())
                }
            }
        }
    }

    @KotlinEventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
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
        fun onGuiOpen(event: TickEvent.ClientTickEvent) {
            // Do the first tick, then unregister self.
            // essentially an on-thread startup complete listener
            val mc = Minecraft.getInstance()
            mc.ingameGUI.chat = GuiNewChatTC
            MinecraftForge.EVENT_BUS.unregister(StartListener)
        }
    }

    @KotlinEventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
    private object NullScreenListener {
        // Listens for a null GuiScreen. Null means we're in-game
        // TODO workaround for lack of client network events. Replace when possible
        @SubscribeEvent
        fun onGuiOpen(event: GuiOpenEvent) {
            if (event.gui == null) {
                // load up the current server's settings
                val connection = mc.connection
                if (connection == null) {
                    serverSettings = null
                } else {
                    val address = connection.networkManager.remoteAddress
                    if (serverSettings?.socket != address) {
                        TabbyChat.logger.info("Loading settings for server $address")
                        serverSettings = ServerSettings(TabbyChat.dataFolder, address).apply {
                            load()

                            // load chat
                            try {
                                ChatManager.loadFrom(path.parent)
                            } catch (e: Exception) {
                                TabbyChat.logger.warn(CHATBOX, "Unable to load chat data.", e)
                            }
                        }
                    }
                }
            }
        }
    }
}
