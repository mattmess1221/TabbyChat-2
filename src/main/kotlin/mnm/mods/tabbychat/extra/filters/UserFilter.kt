package mnm.mods.tabbychat.extra.filters

import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.filters.Filter
import mnm.mods.tabbychat.api.filters.FilterEvent
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.audio.SimpleSound
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.registries.ForgeRegistries
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

class UserFilter(val config: FilterConfig) : Filter {

    val soundLocation get() = ResourceLocation.tryCreate(config.soundName)

    val flags: Set<RegexOption>
        get() {
            val result = HashSet<RegexOption>()
            if (config.ignoreCase) {
                result.add(RegexOption.IGNORE_CASE)
            }
            return result
        }

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

    override val expression: Regex
        get() = resolveRegex()

    private fun resolveRegex(): Regex {
        var expr = resolveVariables(config.expression)
        if (!config.regex) {
            expr = Regex.escape(expr)
        }
        try {
            return Regex(expr, flags);
        } catch (e: PatternSyntaxException) {
            TabbyChat.logger.warn(CHATBOX, "Pattern filter in {} is invalid: {}", config.name, config.expression)
        }
        // null string, probably will never match
        return Regex("\u0000")
    }

    override fun action(event: FilterEvent) {

        if (config.remove) {
            // remove
            event.channels.clear()
        }
        // add channels
        for (dest in config.channels) {
            // replace group tokens in channel name
            val regex = Regex("\\$(\\d+)")
            var name = regex.replace(dest) {
                val group = it.groups[1]!!.value.toInt()
                val groupText = event.matcher.groups[group]
                groupText?.value ?: it.value
            }
            name = ChatManager.coerceChannelName(name)
            ChatManager.parseChannel(name)?.also { event.channels.add(it) }

        }
        // play sound
        soundLocation
            ?.let { ForgeRegistries.SOUND_EVENTS.getValue(it) }
            ?.let { mc.soundHandler.play(SimpleSound.master(it, 1.0f)) }
    }

    override fun prepareText(string: ITextComponent): String {
        return if (config.colors) string.formattedText else super.prepareText(string)
    }
}
