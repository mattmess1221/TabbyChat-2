package mnm.mods.tabbychat.extra.filters

import mnm.mods.tabbychat.api.filters.Filter
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.audio.SimpleSound
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.registries.ForgeRegistries
import org.apache.commons.io.FilenameUtils
import java.nio.file.Path
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import javax.annotation.RegEx

class UserFilter(path: Path) : FileConfigView(path), Filter {

    val name: String
        get() {
            val file = config.nioPath.fileName.toString()
            return FilenameUtils.getBaseName(file)
        }

    val settings by child(::FilterConfig)
    var rawPattern by defining(".*")

    private var expression: Pattern? = null

    override val pattern: Pattern
        get() {
            if (expression == null) {
                this.expression = Pattern.compile(resolvePattern(rawPattern), settings.flags)
            }
            return expression!!
        }

    @Throws(PatternSyntaxException::class)
    fun setPattern(@RegEx pattern: String) {
        testPatternUnsafe(pattern)
        this.rawPattern = pattern
        this.expression = null
    }

    @Throws(UserPatternException::class)
    internal fun testPattern(pattern: String) {
        try {
            testPatternUnsafe(pattern)
        } catch (e: PatternSyntaxException) {
            throw UserPatternException(e)
        }

    }

    @Throws(PatternSyntaxException::class)
    private fun testPatternUnsafe(pattern: String) {
        Pattern.compile(resolvePattern(pattern))
    }

    private fun resolvePattern(pattern: String): String {
        var resolved = resolveVariables(pattern)
        if (!settings.isRegex) {
            resolved = Pattern.quote(resolved)
        }
        return resolved
    }

    @RegEx
    private fun resolveVariables(pattern: String): String {
        val matcher = Pattern.compile("\\$\\{([\\w\\d]+)}").matcher(pattern)
        val buffer = StringBuffer()
        while (matcher.find()) {
            val key = matcher.group(1)
            val `var` = FilterAddon.variables.getOrDefault(key) { "" }()
            matcher.appendReplacement(buffer, `var`)
        }
        matcher.appendTail(buffer)

        return pattern
    }

    override fun action(event: FilterEvent) {

        if (settings.isRemove) {
            // remove
            event.channels.clear()
        }
        // add channels
        for (name in settings.channels) {
            // replace group tokens in channel name
            val matcher = Pattern.compile("\\$(\\d+)").matcher(name)
            var name: String? = name
            while (matcher.find()) {
                // find groups
                val group = Integer.parseInt(matcher.group(1))
                if (group > 0 && event.matcher.groupCount() >= group) {
                    val groupText = event.matcher.group(group)
                    if (groupText != null) {
                        name = name?.replace(matcher.group(), groupText)
                        continue
                    }
                }
                name = null
                break
            }
            // skip this because there were missing or out of bounds groups.
            if (name == null)
                continue

            name = ChatManager.coerceChannelName(name)
            ChatManager.parseChannel(name)?.also { event.channels.add(it) }

        }
        // play sound
        if (settings.isSoundNotification) {
            settings.soundLocation
                    ?.let { ForgeRegistries.SOUND_EVENTS.getValue(it) }
                    ?.let { mc.soundHandler.play(SimpleSound.master(it, 1.0f)) }
        }
    }

    override fun prepareText(string: ITextComponent): String {
        return if (settings.isRaw) string.formattedText else super.prepareText(string)
    }

    internal inner class UserPatternException(e: PatternSyntaxException) : Exception(e)

}
