package mnm.mods.tabbychat.extra.spell;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import mnm.mods.tabbychat.TabbyChat;

public class Spellcheck {

    private SpellChecker spellCheck;
    private Set<String> errors = Sets.newHashSet();

    public Spellcheck() {
        InputStream in = null;
        Reader read = null;
        try {
            in = LangDict.ENGLISH.openStream();
            read = new InputStreamReader(in);
            SpellDictionary dictionary = new SpellDictionaryHashMap(read);
            spellCheck = new SpellChecker(dictionary);
            spellCheck.addSpellCheckListener(new SpellCheckListener() {
                @Override
                public synchronized void spellingError(SpellCheckEvent event) {
                    errors.add(event.getInvalidWord());
                    // System.out.println(event.getWordContextPosition() + " " +
                    // event.getInvalidWord());
                }
            });
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

    public void checkSpelling(String string) {
        this.errors.clear();
        this.spellCheck.checkSpelling(new StringWordTokenizer(string));
    }

    public boolean isCorrect(String word) {
        return !errors.contains(word);
    }

}
