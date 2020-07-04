package mnm.mods.tabbychat.client.extra.spell

import com.google.common.collect.ImmutableSet
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import mnm.mods.tabbychat.util.div
import org.apache.http.HttpStatus
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpHead
import org.apache.http.impl.client.HttpClients
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.MarkerManager
import java.io.BufferedReader
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.UnaryOperator
import java.util.zip.GZIPInputStream

interface WordLists {
    fun getWordList(locale: Locale): WordList?

    fun loadWords(wordList: WordList): BufferedReader

    fun getMissingLocales(): Set<Locale>

    fun downloadAll(locales: Collection<Locale>) = downloadAll(*locales.toTypedArray())

    fun downloadAll(vararg locales: Locale): CompletableFuture<List<CompletableFuture<WordList>>>
}

data class WordList(
        val locale: String,
        val file: String
)

class WordListDownloader(dataFolder: Path, private val syncExecutor: Executor) : WordLists {

    companion object {
        private val logger = LogManager.getLogger()
        private val marker = MarkerManager.getMarker("spelling")
        private val gson = GsonBuilder().setPrettyPrinting().create()
        private val DICS_JSON = "dics.json"
        private val GITHUB_URL = "https://github.com/killjoy1221/aspell-dump/blob/master/dics/%s/%s.dic.gz?raw=true"
    }

    private val dicsDir = dataFolder.resolve("dics")

    private val missingLocales = HashSet<Locale>()
    private val wordLists = HashMap<String, String>()

    init {
        loadWordLists()
    }

    private fun getLocales(prefer: Locale) = listOf(prefer, Locale.getDefault(), Locale.ENGLISH).distinct()

    private fun loadWordLists() {
        wordLists.clear()
        val path = dicsDir / DICS_JSON
        if (Files.exists(path)) {
            Files.newBufferedReader(path).use {
                val obj = gson.fromJson(it, JsonObject::class.java)
                for (e in obj.entrySet()) {
                    wordLists[e.key] = e.value.asString
                }
            }
        }
    }

    private fun saveWordLists() {
        val obj = JsonObject()
        for (e in wordLists.entries) {
            obj.addProperty(e.key, e.value)
        }
        val path = dicsDir / DICS_JSON
        Files.createDirectories(dicsDir)
        Files.newBufferedWriter(path).use {
            gson.toJson(obj, it)
        }
    }

    override fun getWordList(locale: Locale): WordList? {
        logger.info(marker, "Getting dic locale: {}", locale)

        for (l in getLocales(locale)) {
            val wl = findWordList(l)
            if (wl != null) {
                if (Files.exists(wl.path)) {
                    return wl
                }

                logger.warn(marker, "Spellcheck wordlist file is missing! '{}'", wl.path)
                wordLists.remove(l.toString())
                saveWordLists()
            }
        }
        return null
    }

    private fun findWordList(locale: Locale): WordList? {
        val key = locale.toString()
        if (key in wordLists) {
            return WordList(key, wordLists[key]!!)
        }
        logger.warn(marker, "Missing spellcheck dictionary for '{}'. Download it from the spellcheck settings menu.", key)
        missingLocales += locale
        return null
    }

    override fun loadWords(wordList: WordList): BufferedReader {
        return GZIPInputStream(Files.newInputStream(wordList.path)).bufferedReader()
    }

    private val WordList.path: Path get() = dicsDir.resolve(file)

    override fun downloadAll(vararg locales: Locale): CompletableFuture<List<CompletableFuture<WordList>>> {
        return CompletableFuture.supplyAsync {
            HttpClients.createSystem().use { client ->
                val futures = locales.map { locale ->
                    CompletableFuture
                            .supplyAsync { downloadWordList(client, locale) }
                            .thenApplyAsync(UnaryOperator<WordList> {
                                wordLists[it.locale] = it.file
                                missingLocales.remove(locale)
                                saveWordLists()
                                it
                            }, syncExecutor)
                }
                allOf(*futures.toTypedArray()).join()
            }
        }
    }

    /**
     * https://stackoverflow.com/questions/35809827
     */
    private fun <T : Any> allOf(vararg futures: CompletableFuture<T>): CompletableFuture<List<CompletableFuture<T>>> {
        return CompletableFuture.allOf(*futures).thenApply {
            saveWordLists()
            futures.toList()
        }
    }

    override fun getMissingLocales(): Set<Locale> {
        return ImmutableSet.copyOf(missingLocales)
    }

    private fun downloadWordList(client: HttpClient, locale: Locale): WordList {
        val lang = locale.language
        try {
            var url = URI.create(GITHUB_URL.format(lang, locale))
            if (!tryConnectHead(client, url)) {
                val suppressed = IOException("First URL: $url")
                url = URI.create(GITHUB_URL.format(lang, lang))
                if (!tryConnectHead(client, url)) {
                    val suppressed2 = IOException("Second URL: $url")

                    throw IOException("No word list found for $locale").apply {
                        addSuppressed(suppressed)
                        addSuppressed(suppressed2)
                    }
                }
            }

            val path = Paths.get(lang, "$locale.tar.gz")
            downloadFile(client, url, dicsDir.resolve(path))

            return WordList(locale.toString(), path.toString())
        } catch (e: Exception) {
            logger.catching(e)
            throw e
        }
    }

    private fun tryConnectHead(client: HttpClient, url: URI): Boolean {
        return client.execute(HttpHead(url)) {
            it.statusLine.statusCode == HttpStatus.SC_OK
        }
    }

    private fun downloadFile(client: HttpClient, url: URI, file: Path) {
        return client.execute(HttpGet(url)) {
            if (it.statusLine.statusCode != HttpStatus.SC_OK) {
                throw IOException()
            }
            Files.createDirectories(file.parent)
            Files.copy(it.entity.content, file, StandardCopyOption.REPLACE_EXISTING)
        }
    }
}

