package mnm.mods.tabbychat.util

import com.google.common.collect.Lists
import mnm.mods.tabbychat.api.Message
import mnm.mods.tabbychat.client.ChatMessage
import mnm.mods.tabbychat.client.TabbyChatClient
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.RenderComponentsUtil
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

object ChatTextUtils {

    fun split(chat: ITextComponent, width: Int): List<ITextComponent> {
        val fr = Minecraft.getInstance().fontRenderer
        return RenderComponentsUtil.splitText(chat, width, fr, false, false)
    }

    fun split(list: List<ChatMessage>, width: Int): List<ChatMessage> {
        if (width <= 8)
        // ignore, characters are larger than width
            return Lists.newArrayList(list)
        // prevent concurrent modification caused by chat thread
        synchronized(list) {
            val result = Lists.newArrayList<ChatMessage>()
            val iter = list.iterator()
            while (iter.hasNext() && result.size <= 100) {
                val line = iter.next()
                val chatlist = split(getMessageWithOptionalTimestamp(line), width)
                for (i in chatlist.indices.reversed()) {
                    val chat = chatlist[i]
                    result.add(ChatMessage(line.counter, chat, line.id, null))
                }
            }
            return result
        }
    }

    fun getMessageWithOptionalTimestamp(msg: Message): ITextComponent {
        val settings = TabbyChatClient.settings.general
        val date = msg.dateTime
        if (date != null && settings.timestampChat.value) {
            val stamp = settings.timestampStyle.value
            val format = settings.timestampColor.value
            return "${stamp.format(date)} ".toComponent().style {
                color = format
            }.appendSibling(msg.message)
        }
        return msg.message

    }

    /**
     * Returns a ChatComponent that is a sub-component of another one. It begins
     * at the specified index and extends to the end of the componenent.
     *
     * @param chat       The chat to subchat
     * @param beginIndex The beginning index, inclusive
     * @return The end of the chat
     * @see String.substring
     */
    fun subChat(chat: ITextComponent, beginIndex: Int): ITextComponent? {
        var rchat: ITextComponent? = null
        val ichat = chat.iterator()
        var pos = 0
        while (ichat.hasNext()) {
            var part = ichat.next()
            val s = part.unformattedComponentText

            val len = s.length
            if (len + pos >= beginIndex) {
                if (pos < beginIndex) {
                    val schat = StringTextComponent(s.substring(beginIndex - pos))
                    schat.style = part.style.createShallowCopy()
                    part = schat
                }
                if (rchat == null) {
                    rchat = part
                } else {
                    rchat.appendSibling(part)
                }
            }
            pos += len
        }
        return rchat
    }
}
