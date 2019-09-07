package mnm.mods.tabbychat.client.extra.spell

import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.TabbyChat
import net.minecraft.client.Minecraft
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
                { Files.newInputStream(TabbyChat.dataFolder.resolve(path)) } as LangDict
            } else {
                val res = ResourceLocation(MODID, path)
                val resmgr = Minecraft.getInstance().resourceManager
                { resmgr.getResource(res).inputStream } as LangDict
            }
        }
    }
}
