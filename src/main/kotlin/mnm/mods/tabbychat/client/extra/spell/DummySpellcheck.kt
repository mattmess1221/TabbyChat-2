package mnm.mods.tabbychat.client.extra.spell

import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent

object DummySpellcheck : Spellcheck {
    override fun checkSpelling(string: String): (String) -> ITextComponent? = ::StringTextComponent
    override fun loadCurrentLanguage() = Unit
}
