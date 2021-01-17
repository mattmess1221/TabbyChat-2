package mnm.mods.tabbychat.extra.spell

import com.swabunga.spell.event.SpellCheckEvent
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.Colors
import mnm.mods.tabbychat.util.text.FancyText
import mnm.mods.tabbychat.util.toComponent
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

class SpellingFormatter(private val spelling: Iterator<SpellCheckEvent>) : (String) -> ITextComponent {

    private var event: SpellCheckEvent? = null
    private var totalLength: Int = 0

    val config get() = TabbyChatClient.settings.spellcheck

    override operator fun invoke(text: String): ITextComponent {
        if (text.contains("\u00a7") || !config.enabled) {
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

            val style = {FancyText.FancyStyle(underline = Colors.RED)}

            if (end > text.length) {
                // error goes to next line
                return b.appendSibling(FancyText(text.substring(start).toComponent()).fancyStyle(style))
            }
            b.appendSibling(FancyText(text.substring(start, end).toComponent()).fancyStyle(style))
            prev = end
            event = null
        }
        // no more errors.
        totalLength++
        return b.appendText(text.substring(prev))
    }
}