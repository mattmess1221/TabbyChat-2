package mnm.mods.tabbychat.client.extra.spell

import net.minecraft.resources.IResourceManager
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraftforge.resource.IResourceType
import java.util.function.Predicate

object DummySpellcheck : Spellcheck {
    override fun checkSpelling(string: String): (String) -> ITextComponent? = ::StringTextComponent
    override fun loadCurrentLanguage() = Unit
    override fun onResourceManagerReload(resourceManager: IResourceManager, resourcePredicate: Predicate<IResourceType>) = Unit
}
