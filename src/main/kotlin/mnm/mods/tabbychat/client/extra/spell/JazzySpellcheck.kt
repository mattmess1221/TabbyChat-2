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
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.function.Predicate

internal class JazzySpellcheck : Spellcheck, ISelectiveResourceReloadListener {

    val userDictionaryFile: Path = TabbyChat.dataFolder.resolve("userdict.txt")

    private var spellCheck: SpellChecker? = null
    private var userDict: SpellDictionary? = null
    private val errors = mutableListOf<SpellCheckEvent>()

    @Synchronized
    @Throws(IOException::class)
    fun loadDictionary(lang: LangDict): SpellChecker {
        InputStreamReader(openLangStream(lang)).use { read ->
            val dictionary = SpellDictionaryHashMap(read)
            return SpellChecker(dictionary).apply {
                setUserDictionary(userDict)
                addSpellCheckListener { errors.add(it) }
            }
        }
    }

    @Throws(IOException::class)
    private fun openLangStream(lang: LangDict): InputStream {
        return try {
            lang.openStream()
        } catch (e: FileNotFoundException) {
            if (lang === LangDict.ENGLISH) {
                // Prevent StackOverflowException
                throw e
            }
            TabbyChat.logger.warn(SPELLCHECK, "Error loading dictionary. Falling back to en_us.", e)
            openLangStream(LangDict.ENGLISH)
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
        this.userDict!!.addWord(word)
    }

    override fun checkSpelling(string: String): (String?) -> ITextComponent? {
        if (spellCheck != null) {
            this.errors.clear()
            this.spellCheck!!.checkSpelling(StringWordTokenizer(string))
        }

        return SpellingFormatter(this.errors.iterator())
    }

    override fun onResourceManagerReload(resourceManager: IResourceManager, resourcePredicate: Predicate<IResourceType>) {
        loadCurrentLanguage()
    }

    override fun loadCurrentLanguage() {
        val lang = mc.languageManager.currentLanguage
        try {
            userDict = loadUserDictionary()
            spellCheck = loadDictionary(LangDict.fromLanguage(lang.code)).apply {
                addDictionary(userDict)
            }
        } catch (e: IOException) {
            TabbyChat.logger.warn(SPELLCHECK, "Error while loading dictionary ${lang.code}.", e)
            spellCheck = null
            userDict = null
        }
    }

}
