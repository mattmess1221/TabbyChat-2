package mnm.mods.tabbychat.util.text

import com.google.common.collect.Streams
import mnm.mods.tabbychat.util.Color
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponent
import java.util.stream.Stream

class FancyText(val text: ITextComponent) : TextComponent() {

    data class FancyStyle(var color: Color = Color.WHITE,
                          var underline: Color = Color(0),
                          var highlight: Color = Color(0)) {
        fun copy(other: FancyStyle): FancyStyle {
            return FancyStyle(other.color, other.underline, other.highlight)
        }
    }

    var fancyStyle = FancyStyle()

    fun fancyStyle(block: FancyStyle.() -> Unit) = apply {
        fancyStyle.block()
    }

    init {
        require(text !is FancyText) { "Parent text cannot be fancy" }
    }

    override fun getUnformattedComponentText(): String = text.unformattedComponentText

    override fun shallowCopy(): ITextComponent = FancyText(text.shallowCopy()).also {
        it.fancyStyle = fancyStyle.copy()
    }

    override fun iterator(): MutableIterator<ITextComponent> {
        // don't iterate using the vanilla components
        return this.text.asSequence()
                .map {
                    it as? FancyText ?: FancyText(it).also { text ->
                        text.fancyStyle = this.fancyStyle.copy()
                    }
                }.iterator() as MutableIterator
    }

    override fun stream(): Stream<ITextComponent> {
        return Streams.concat(Stream.of(this), getSiblings().stream().flatMap { it.stream() })
    }

    override fun toString(): String {
        return String.format("FancyText{text=%s, fancystyle=%s}", text, style)
    }
}
