package mnm.mods.tabbychat.client.extra.spell

import com.swabunga.spell.engine.SpellDictionary
import com.swabunga.spell.engine.SpellDictionaryHashMap
import com.swabunga.spell.event.SpellCheckEvent
import com.swabunga.spell.event.SpellChecker
import com.swabunga.spell.event.StringWordTokenizer
import mnm.mods.tabbychat.SPELLCHECK
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.util.mc
import net.minecraft.resources.IResourceManager
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.resource.IResourceType
import net.minecraftforge.resource.ISelectiveResourceReloadListener
import net.minecraftforge.resource.VanillaResourceType
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.function.Predicate

internal class JazzySpellcheck(dataFolder: Path, val wordLists: WordLists) : Spellcheck, ISelectiveResourceReloadListener {

    private val userDictionaryFile: Path = dataFolder.resolve("userdict.txt")

    private lateinit var spellCheck: SpellChecker
    private lateinit var userDict: SpellDictionary
    private val errors = mutableListOf<SpellCheckEvent>()

    private val delayedExecutor = Executors.newSingleThreadScheduledExecutor()

    @Synchronized
    @Throws(IOException::class)
    fun loadDictionary(lang: WordList?): SpellChecker {
        val dictionary = lang?.let {
            wordLists.loadWords(it).use { read ->
                SpellDictionaryHashMap(read)
            }
        } ?: SpellDictionaryHashMap()
        return SpellChecker(dictionary).apply {
            setUserDictionary(userDict)
            addSpellCheckListener { errors.add(it) }
        }
    }

    @Synchronized
    @Throws(IOException::class)
    fun loadUserDictionary(): SpellDictionary {
        if (Files.notExists(userDictionaryFile)) {
            Files.createDirectories(userDictionaryFile.parent)
            Files.createFile(userDictionaryFile)
            Files.newBufferedWriter(userDictionaryFile).use {
                it.write("# User dictionary, one entry per line.")
            }
        }
        return Files.newBufferedReader(userDictionaryFile).use {
            UserDictionary(it)
        }
    }

    @Synchronized
    fun addToDictionary(word: String) {
        // add to user dictionary
        this.userDict.addWord(word)
    }

    override fun checkSpelling(string: String): (String?) -> ITextComponent? {
        this.errors.clear()
        this.spellCheck.checkSpelling(StringWordTokenizer(string))

        return SpellingFormatter(this.errors.iterator())
    }

    override fun onResourceManagerReload(resourceManager: IResourceManager, resourcePredicate: Predicate<IResourceType>) {
        if (resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            loadCurrentLanguage()

            mc.execute {
                // the loading gui interferes with the toast,
                delayedExecutor.schedule({
                    mc.execute {
                        wordLists.alertMissingWordLists()
                    }
                }, 3, TimeUnit.SECONDS)
            }
        }
    }

    override fun loadCurrentLanguage() {
        // LanguageManager.currentLanguage is null during startup because languages haven't been
        // loaded yet. I just need the code, and it's the same as the settings language anyway.
        val lang = mc.gameSettings.language
        try {
            val wordList = wordLists.getWordList(lang.toLocale())
            userDict = loadUserDictionary()
            spellCheck = loadDictionary(wordList)
            spellCheck.addDictionary(userDict)

        } catch (e: IOException) {
            TabbyChat.logger.warn(SPELLCHECK, "Error while loading dictionary ${lang}.", e)
        }
    }

    private fun String.toLocale(): Locale {
        val parts = this.split("_", limit = 2)
        return if (parts.size == 1) {
            val (code) = parts
            Locale(code)
        } else {
            val (code, country) = parts
            Locale(code,country.toUpperCase())
        }
    }

}
