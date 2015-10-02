package mnm.mods.tabbychat.extra.spell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.client.Minecraft;

import com.google.common.collect.Maps;

public class LangDict {

    private static final Map<String, LangDict> cache = Maps.newHashMap();

    public static final LangDict ENGLISH = getLang("en_US");

    private String path;

    private LangDict(String s) {
        path = s;
    }

    public boolean isClasspath() {
        return getClass().getClassLoader().getResource("dicts/" + path + "x.dic") != null;
    }

    public boolean isConfig() {
        File lang = new File(TabbyChat.getInstance().getDataFolder(), getPath());
        return lang.exists() && lang.isFile();
    }

    public InputStream openStream() throws IOException {
        InputStream in;
        if (isClasspath()) {
            in = getClass().getClassLoader().getResourceAsStream(getPath());
        } else if (isConfig()) {
            File f = new File(TabbyChat.getInstance().getDataFolder(), getPath());
            in = new FileInputStream(f);
        } else {
            // it doesn't exist.
            in = ENGLISH.openStream();
        }
        return in;
    }

    private String getPath() {
        return String.format("dicts/%sx.dic", path);
    }

    @Override
    public String toString() {
        return path;
    }

    public static LangDict getLang(String id) {
        if (!cache.containsKey(id)) {
            cache.put(id, new LangDict(id));
        }
        return cache.get(id);
    }

    public static LangDict getLang() {
        return getLang(Minecraft.getMinecraft().gameSettings.language);
    }
}
