package mnm.mods.tabbychat.client.extra.spell

import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.util.mc
import net.minecraft.util.ResourceLocation
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files

interface LangDict {

    @Throws(IOException::class)
    fun openStream(): InputStream

    companion object {

        val ENGLISH = fromLanguage("en_us")

        fun fromLanguage(lang: String): LangDict {
            val path = String.format("dicts/%s.dic", lang)
            return if (Files.isRegularFile(TabbyChat.dataFolder.resolve(path))) {
                LangDict { Files.newInputStream(TabbyChat.dataFolder.resolve(path)) }
            } else {
                val res = ResourceLocation(MODID, path)
                val resmgr = mc.resourceManager
                LangDict { resmgr.getResource(res).inputStream }
            }
        }

        operator fun invoke(function: () -> InputStream) = object : LangDict {
            override fun openStream() = function()
        }
    }
}
