package mnm.mods.tabbychat.util

import net.minecraft.client.resources.I18n
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TranslationTextComponent

inline class Translatable(private val key: String) {
    fun toComponent(vararg args: Any): ITextComponent {
        return TranslationTextComponent(this.key, *args)
    }

    fun translate(vararg args: Any): String {
        return I18n.format(this.key, *args)
    }
}