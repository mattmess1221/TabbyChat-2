package mnm.mods.tabbychat.forge;

import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.internal.InternalAPI;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "tabbychat2", name = "TabbyChat 2 (Forge Compatibility)", clientSideOnly = true)
public class FMLTabbyChat {

    @EventHandler 
    public void preInit(FMLPostInitializationEvent event) {
        InternalAPI tc = TabbyAPI.getAPI().cast();
        tc.setForgeProxy(new ForgeProxyImpl());
    }
}
