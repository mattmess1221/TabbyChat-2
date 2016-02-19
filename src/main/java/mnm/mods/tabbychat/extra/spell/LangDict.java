package mnm.mods.tabbychat.extra.spell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.Validate;

import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.client.resources.Language;

public class LangDict {

    public static final LangDict ENGLISH = new LangDict("en_US");

    private final String language;

    LangDict(String lang) {
        Validate.notNull(lang, "Language cannot be null!");
        language = lang;
    }

    public boolean isClasspath() {
        return getClass().getClassLoader().getResource(getPath()) != null;
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
            throw new FileNotFoundException("Dictionary for " + language + " does not exist.");
        }
        return in;
    }

    private String getPath() {
        return String.format("dicts/%sx.dic", language);
    }

    @Override
    public int hashCode() {
        return language.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof LangDict))
            return false;
        LangDict other = (LangDict) obj;
        return language.equals(other.language);
    }

    public static LangDict fromLanguage(Language lang) {
        String code = "en_US";
        if (lang != null)
            code = lang.getLanguageCode();
        return new LangDict(code);
    }
}
