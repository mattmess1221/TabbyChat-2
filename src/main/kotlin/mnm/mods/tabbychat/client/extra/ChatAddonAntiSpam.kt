package mnm.mods.tabbychat.client.extra

import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.ChatChannel
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.TabbyChatClient
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.apache.commons.lang3.StringUtils

object ChatAddonAntiSpam {

    private val messageMap= mutableMapOf<Channel, Counter>()

    @SubscribeEvent
    fun onMessageAdded(event: MessageAddedToChannelEvent.Pre) {

        val enabled = TabbyChatClient.settings.general.antiSpam.value
        val prejudice = TabbyChatClient.settings.general.antiSpamPrejudice.value

        if (enabled && event.id == 0) {
            val channel = event.channel as ChatChannel
            val counter = messageMap.getOrPut(channel) { Counter() }
            val text = event.text
            if (text != null) {
                val chat = text.string

                if (getDifference(chat, counter.lastMessage) <= prejudice) {
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
        internal var lastMessage = ""
        internal var spamCounter = 1
    }

    private fun getDifference(s1: String, s2: String): Double {
        val avgLen = (s1.length + s2.length) / 2.0
        return if (avgLen == 0.0) {
            0.0
        } else StringUtils.getLevenshteinDistance(s1.toLowerCase(), s2.toLowerCase()) / avgLen
    }
}
