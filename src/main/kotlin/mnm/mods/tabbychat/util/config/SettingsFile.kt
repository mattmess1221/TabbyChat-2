package mnm.mods.tabbychat.util.config

import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonParseException
import mnm.mods.tabbychat.CONFIG
import mnm.mods.tabbychat.TabbyChat
import net.minecraft.util.EnumTypeAdapterFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path

/**
 * Used for creating settings and saving/loading them in the JSON format. Start
 * by creating fields. Mark anything you don't wish to be serialized with `transient`.
 *
 * @author Matthew Messinger
 */
abstract class SettingsFile(@field:Transient val path: Path) : ValueObject() {

    @Transient
    private val gson = GsonBuilder()
            .registerTypeAdapter(javaClass, { this } as InstanceCreator<*>)
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
            TabbyChat.logger.error(CONFIG, "Failed to save config to {}", path, e)
        }

    }

    fun load() {

        try {
            Files.newBufferedReader(path).use {
                gson.fromJson(it, javaClass)
            }
        } catch (e: NoSuchFileException) {
            TabbyChat.logger.info(CONFIG, "Saving default config to {}", path)
            save()
        } catch (e: IOException) {
            TabbyChat.logger.error(CONFIG, "Failed to load config from {}", path, e)
        } catch (e: JsonParseException) {
            TabbyChat.logger.error(CONFIG, "Syntax or schema error in {}", path, e)
        }

    }

}
