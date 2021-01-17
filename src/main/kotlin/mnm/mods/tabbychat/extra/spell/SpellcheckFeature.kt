package mnm.mods.tabbychat.extra.spell

import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.util.config.ConfigManager
import mnm.mods.tabbychat.util.div
import mnm.mods.tabbychat.util.mc
import net.minecraft.resources.IReloadableResourceManager
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.forge.runWhenOn

@Mod.EventBusSubscriber(modid = MODID, value = [Dist.CLIENT])
object SpellcheckFeature {

    init {
        runWhenOn(Dist.CLIENT) {
            val resources = mc.resourceManager as IReloadableResourceManager
            resources.addReloadListener(Spellcheck)
        }
    }
}