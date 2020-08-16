package mnm.mods.tabbychat.client.extra.spell

import com.swabunga.spell.event.SpellChecker
import mnm.mods.tabbychat.STARTUP
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.util.mc
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.resource.ISelectiveResourceReloadListener

interface Spellcheck : ISelectiveResourceReloadListener {
    fun checkSpelling(string: String): (String) -> ITextComponent?
    fun loadCurrentLanguage()

    companion object {
        private val instance by lazy {
            try {
                // throws NoClassDefFoundError if using the wrong jar.
                checkNotNull(SpellChecker::class)
                val wordLists = WordListDownloader(TabbyChat.dataFolder, mc)
                JazzySpellcheck(TabbyChat.dataFolder, wordLists)
            } catch (e: NoClassDefFoundError) {
                TabbyChat.logger.warn(STARTUP, "Spellcheck could not be initialized. Are you using the correct jar?", e)
                DummySpellcheck
            }
        }

        operator fun invoke(): Spellcheck {
            return instance
        }
    }
}
