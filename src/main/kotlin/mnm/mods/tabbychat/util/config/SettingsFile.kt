package mnm.mods.tabbychat.util.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.minecraft.util.EnumTypeAdapterFactory
import org.apache.logging.log4j.LogManager
import java.io.IOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path

/**
 * Used for creating settings and saving/loading them in the JSON format. Start
 * by creating delegate properties.
 */
abstract class SettingsFile<T : SettingsFile<T>>(val path: Path) : ValueObject<T>() {

    companion object {
        private val logger = LogManager.getLogger()
    }

    private val gson = GsonBuilder()
            .registerTypeAdapter(this::class.java, Serializer())
            .setPrettyPrinting()
            .registerTypeAdapterFactory(EnumTypeAdapterFactory())
            .create()

    fun save() {
        try {
            Files.createDirectories(path.parent)
            Files.newBufferedWriter(path).use {
                gson.toJson(this, it)
            }
        } catch (e: IOException) {
            logger.error("Failed to save config to {}", path, e)
        }
    }

    fun load() {
        try {
            Files.newBufferedReader(path).use {
                gson.fromJson(it, javaClass)
            }
        } catch (e: NoSuchFileException) {
            logger.info("Saving default config to {}", path)
            save()
        } catch (e: IOException) {
            logger.error("Failed to load config from {}", path, e)
        } catch (e: JsonParseException) {
            logger.error("Syntax or schema error in {}", path, e)
        }

    }

    private inner class Serializer : TypeAdapter<SettingsFile<T>>() {
        override fun read(reader: JsonReader): SettingsFile<T> {
            readObject(reader, properties)
            return this@SettingsFile
        }

        fun readObject(reader: JsonReader, root: MutableMap<String, AbstractValue<out Any>>) {
            reader.beginObject()
            while (reader.hasNext()) {
                when (val value = root[reader.nextName()]) {
                    is Value -> value.value = gson.fromJson(reader, value.type.type)
                    is ValueList<*> -> {
                        value.clear()
                        value.addAll(gson.fromJson(reader, value.type.type))
                    }
                    is ValueMap<*> -> {
                        value.clear()
                        value.putAll(gson.fromJson(reader, value.type.type))
                    }
                    is ValueObject -> readObject(reader, value.properties)
                }
            }
            reader.endObject()
        }

        override fun write(writer: JsonWriter, obj: SettingsFile<T>) {
            writeObject(writer, obj.properties)
        }

        fun writeObject(writer: JsonWriter, root: MutableMap<String, AbstractValue<out Any>>) {
            writer.beginObject()
            for ((key, value: AbstractValue<out Any>) in root) {
                writer.name(key)
                when (value) {
                    is Value<*> -> gson.toJson(value.value, value.type.type, writer)
                    is ValueList<*> -> gson.toJson(value.value, value.type.type, writer)
                    is ValueMap<*> -> gson.toJson(value.value, value.type.type, writer)
                    is ValueObject<*> -> writeObject(writer, value.properties)
                }
            }
            writer.endObject()
        }
    }
}