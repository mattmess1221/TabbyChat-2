package mnm.mods.tabbychat.client.extra.spell

import com.swabunga.spell.event.SpellCheckEvent
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.text.FancyText
import mnm.mods.tabbychat.util.toComponent
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

class SpellingFormatter(sp: Spellcheck) {

    private val spelling: Iterator<SpellCheckEvent> = sp.getErrors().iterator()

    private var event: SpellCheckEvent? = null
    private var totalLength: Int = 0

    fun apply(text: String?): ITextComponent? {
        if (text == null)
            return null
        if (text.contains("\u00a7") || !TabbyChatClient.settings.advanced.spelling) {
            return StringTextComponent(text)
        }

        val b = StringTextComponent("")
        var prev = 0
        val length = totalLength
        // save where we are at.
        totalLength += text.length
        while (spelling.hasNext() || event != null) {
            if (event == null)
                event = spelling.next()
            var start = event!!.wordContextPosition - length
            val end = start + event!!.invalidWord.length

            if (start < 0) {
                // error started on previous line
                start = prev
            }
            if (start > text.length) {
                // no more errors on this line
                break
            }
            b.appendText(text.substring(prev, start))

            if (end > text.length) {
                // error goes to next line
                return b.appendSibling(FancyText(text.substring(start).toComponent()).fancyStyle {
                    underline = Color.RED
                })
            }
            b.appendSibling(FancyText(text.substring(start, end).toComponent()).fancyStyle {
                underline = Color.RED

                prev = end
                event = null
            })
        }
        // no more errors.
        totalLength++
        return b.appendText(text.substring(prev))
    }
}