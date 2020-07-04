package mnm.mods.tabbychat.client.extra.spell

import com.mojang.bridge.game.Language
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
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.function.Predicate

internal class JazzySpellcheck(dataFolder: Path, val wordLists: WordLists) : Spellcheck, ISelectiveResourceReloadListener {

    private val userDictionaryFile: Path = dataFolder.resolve("userdict.txt")

    private lateinit var spellCheck: SpellChecker
    private lateinit var userDict: SpellDictionary
    private val errors = mutableListOf<SpellCheckEvent>()

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
        loadCurrentLanguage()
    }

    override fun loadCurrentLanguage() {
        val lang = mc.languageManager.currentLanguage
        try {

            val wordList = wordLists.getWordList(lang.toLocale())
            userDict = loadUserDictionary()
            spellCheck = loadDictionary(wordList)
            spellCheck.addDictionary(userDict)

        } catch (e: IOException) {
            TabbyChat.logger.warn(SPELLCHECK, "Error while loading dictionary ${lang.code}.", e)
        }
    }

    private fun Language.toLocale(): Locale {
        // forge bug causes the javaLocale to be badly formed.
        // FIXME MinecraftForge#6861
        val parts = this.code.split("_".toRegex(), 2)
        return if (parts.size == 1) {
            Locale(parts[0])
        } else {
            Locale(parts[0], parts[1].toUpperCase())
        }
    }

}
