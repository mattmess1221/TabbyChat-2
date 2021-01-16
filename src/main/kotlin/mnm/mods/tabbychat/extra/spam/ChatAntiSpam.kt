package mnm.mods.tabbychat.extra.spam

import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.util.config.ConfigManager
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import org.apache.commons.lang3.StringUtils

@Mod.EventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
object ChatAntiSpam {

    private val messageMap = mutableMapOf<Channel, Counter>()

    val config = ChatAntiSpamConfig(TabbyChat.dataFolder)

    init {
        ConfigManager.addConfigs(config)
    }

    @SubscribeEvent
    fun onMessageAdded(event: MessageAddedToChannelEvent.Pre) {

        if (config.antiSpam && event.id == 0) {
            val channel = event.channel
            val counter = messageMap.getOrPut(channel) { Counter() }
            val text = event.text
            if (text != null) {
                val chat = text.string

                if (getDifference(chat, counter.lastMessage) <= config.antiSpamPrejudice) {
                    counter.spamCounter++
                    text.appendText(" [" + counter.spamCounter + "x]")
                    ChatManager.removeMessageAt(channel, 0)
                    ChatManager.markDirty(channel)
                } else {
                    counter.lastMessage = chat
                    counter.spamCounter = 1
                }
            }
        }
    }

    private class Counter {
        var lastMessage = ""
        var spamCounter = 1
    }

    private fun getDifference(s1: String, s2: String): Double {
        val avgLen = (s1.length + s2.length) / 2.0
        return if (avgLen == 0.0) {
            0.0
        } else StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase()) / avgLen
    }
}
