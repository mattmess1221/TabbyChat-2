package mnm.mods.tabbychat.extra.spell;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.common.io.Closeables;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellChecker;

import mnm.mods.tabbychat.TabbyChat;

public class Spellcheck {

    private SpellChecker spellCheck;

    public Spellcheck(LangDict lang) {
        InputStream in = null;
        Reader read = null;
        try {
            in = lang.openStream();
            read = new InputStreamReader(in);
            SpellDictionary dictionary = new SpellDictionaryHashMap(read);
            spellCheck = new SpellChecker(dictionary);
        } catch (IOException e) {
            TabbyChat.getLogger().warn("Error whie loading dictionary.", e);
        } finally {
            Closeables.closeQuietly(in);
            Closeables.closeQuietly(read);
        }
    }

    public List<String> getSuggestions(String word, int threshold) {
        return this.spellCheck.getSuggestions(word, threshold);
    }

    public boolean isCorrect(String word) {
        return this.spellCheck.isCorrect(word);
    }
}
