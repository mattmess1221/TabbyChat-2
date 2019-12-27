package mnm.mods.tabbychat.client.extra.spell

import com.google.common.collect.Lists
import com.swabunga.spell.engine.SpellDictionary
import com.swabunga.spell.engine.SpellDictionaryHashMap
import com.swabunga.spell.event.SpellCheckEvent
import com.swabunga.spell.event.SpellChecker
import com.swabunga.spell.event.StringWordTokenizer
import mnm.mods.tabbychat.SPELLCHECK
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.util.mc
import net.minecraft.resources.IResourceManager
import net.minecraftforge.resource.IResourceType
import net.minecraftforge.resource.ISelectiveResourceReloadListener
import net.minecraftforge.resource.VanillaResourceType
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.function.Predicate

class Spellcheck(configDir: Path) : ISelectiveResourceReloadListener {

    val userDictionaryFile: Path = configDir.resolve("userdict.txt")

    private var spellCheck: SpellChecker? = null
    private var userDict: SpellDictionary? = null
    private val errors = Lists.newArrayList<SpellCheckEvent>()

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

    fun getErrors(): Iterable<SpellCheckEvent> {
        return Collections.unmodifiableList(this.errors)
    }

    fun checkSpelling(string: String) {
        if (spellCheck != null) {
            this.errors.clear()
            this.spellCheck!!.checkSpelling(StringWordTokenizer(string))
        }
    }

    override fun onResourceManagerReload(resourceManager: IResourceManager, resourcePredicate: Predicate<IResourceType>) {
        if (resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            loadCurrentLanguage()
        }
    }

    private fun loadCurrentLanguage() {
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
