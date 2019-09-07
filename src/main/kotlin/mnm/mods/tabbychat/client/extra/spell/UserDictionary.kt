package mnm.mods.tabbychat.client.extra.spell

import java.io.BufferedReader
import java.io.IOException
import java.io.Reader
import java.io.UncheckedIOException

import com.swabunga.spell.engine.SpellDictionaryHashMap

/**
 * Dictionary that supports `#` comments
 */
class UserDictionary
@Throws(IOException::class)
internal constructor(reader: Reader) : SpellDictionaryHashMap(reader) {

    @Throws(IOException::class)
    override fun createDictionary(reader: BufferedReader) {
        try {
            reader.lineSequence()
                    .filter { line -> line.startsWith("#") && line.trim { it <= ' ' }.isNotEmpty() }
                    .forEach { this.putWord(it) }
        } catch (e: UncheckedIOException) {
            throw e.cause!!
        }

    }
}
