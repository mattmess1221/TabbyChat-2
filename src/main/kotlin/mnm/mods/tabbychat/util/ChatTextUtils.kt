package mnm.mods.tabbychat.util

import mnm.mods.tabbychat.api.Message
import mnm.mods.tabbychat.client.ChatMessage
import mnm.mods.tabbychat.client.TabbyChatClient
import net.minecraft.client.gui.RenderComponentsUtil
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

object ChatTextUtils {

    fun split(list: List<ChatMessage>, width: Int): List<ChatMessage> {
        if (width <= 8) {
            // ignore, characters are larger than width
            return list.toMutableList()
        }
        // prevent concurrent modification caused by chat thread
        return synchronized(list) {
            list.asSequence()
                    .flatMap { line ->
                        val msg = getMessageWithOptionalTimestamp(line)
                        val texts = RenderComponentsUtil.splitText(msg, width, mc.fontRenderer, false, false)
                        texts.reverse()
                        texts.asSequence()
                                .map { chat ->
                                    ChatMessage(line.counter, chat, line.id, null)
                                }
                    }
                    .take(100)
                    .toList()
        }
    }

    fun getMessageWithOptionalTimestamp(msg: Message): ITextComponent {
        val settings = TabbyChatClient.settings.general
        val date = msg.dateTime
        if (date != null && settings.timestampChat) {
            val stamp = settings.timestampStyle
            val format = settings.timestampColor
            return "".toComponent()
                    .appendSibling("${stamp.format(date)} ".toComponent().applyTextStyle(format))
                    .appendSibling(msg.message)
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
