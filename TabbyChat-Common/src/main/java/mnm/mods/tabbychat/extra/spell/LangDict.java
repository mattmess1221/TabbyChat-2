package mnm.mods.tabbychat.extra.spell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import mnm.mods.tabbychat.TabbyChat;

public class LangDict {

    public static final LangDict ENGLISH = new LangDict("en_US");

    private String path;

    public LangDict(String s) {
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
}
