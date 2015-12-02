package mnm.mods.tabbychat.forge;

import org.apache.logging.log4j.LogManager;

import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.internal.Compat;
import mnm.mods.tabbychat.api.internal.InternalAPI;
import mnm.mods.tabbychat.api.listener.TabbyListener;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = "tabbychat2", name = "TabbyChat 2 (Forge Compatibility)", clientSideOnly = true)
public class FMLTabbyChat {

    @EventHandler
    public void init(FMLInitializationEvent event) {
        InternalAPI tc = (InternalAPI) TabbyAPI.getAPI();
        tc.setForgeProxy(new ForgeProxyImpl());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // TODO forge mod compats

    }

    @SuppressWarnings("unused")
    private void addCompatibility(String classname) {
        try {
            Class<?> cl = Class.forName(classname);
            if (Loader.isModLoaded(cl.getAnnotation(Compat.class).value())) {
                LogManager.getLogger().info(cl.getSimpleName() + " detected. Adding compatibilities.");
                TabbyListener o = (TabbyListener) cl.newInstance();

                TabbyAPI.getAPI().getAddonManager().registerListener(o);
            }
        } catch (Throwable e) {
            LogManager.getLogger().warn("Unable to add compatibility. Did something change?", e);
        }
    }
}
