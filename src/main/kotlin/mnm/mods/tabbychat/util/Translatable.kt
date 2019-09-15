package mnm.mods.tabbychat.util

import net.minecraft.client.resources.I18n
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent
import kotlin.reflect.KProperty

/**
 * Translatable strings. Translations are done via
 * [I18n.format].
 *
 * @author Matthew
 */
interface Translatable {

    /**
     * Gets the unlocalized string for this translation
     *
     * @return The untranslated string
     */
    val unlocalized: String

    /**
     * Translates this string.
     *
     * @param params Translation parameters
     * @return The translated string
     */
    fun translate(vararg params: Any): String {
        return I18n.format(unlocalized, *params)
    }

    fun toComponent(vararg args: Any) = TranslationTextComponent(unlocalized, *args)

    operator fun getValue(thisRef: Any, property: KProperty<*>) = translate()

    companion object {
        operator fun invoke(unloc: () -> String) = object : Translatable {
            override val unlocalized: String get() = unloc()
        }
    }
}
