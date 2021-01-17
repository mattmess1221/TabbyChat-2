package mnm.mods.tabbychat.extra.spell

import com.swabunga.spell.event.SpellChecker
import mnm.mods.tabbychat.STARTUP
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.util.div
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.resource.ISelectiveResourceReloadListener

interface Spellcheck : ISelectiveResourceReloadListener {
    fun checkSpelling(text: String): ITextComponent
    fun loadCurrentLanguage()

    companion object : Spellcheck by try {
        // throws NoClassDefFoundError if using the wrong jar.
        checkNotNull(SpellChecker::class)
        val spellcheckFolder = TabbyChat.dataFolder / "spellcheck"
        JazzySpellcheck(spellcheckFolder)
    } catch (e: NoClassDefFoundError) {
        TabbyChat.logger.warn(STARTUP, "Spellcheck could not be initialized. Are you using the correct jar?", e)
        DummySpellcheck
    }
}
