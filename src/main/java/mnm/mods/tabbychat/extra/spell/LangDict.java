package mnm.mods.tabbychat.extra.spell;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public interface LangDict {

    LangDict ENGLISH = fromLanguage("en_us");

    InputStream openStream() throws IOException;

    static LangDict fromLanguage(String lang) {
        String path = String.format("dicts/%s.dic", lang);
        if (Files.isRegularFile(TabbyChat.dataFolder.resolve(path))) {
            return () -> Files.newInputStream(TabbyChat.dataFolder.resolve(path));
        } else {
            ResourceLocation res = new ResourceLocation(TabbyChat.MODID, path);
            IResourceManager resmgr = Minecraft.getInstance().getResourceManager();
            return () -> resmgr.getResource(res).getInputStream();
        }
    }
}
