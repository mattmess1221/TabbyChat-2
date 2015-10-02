package mnm.mods.tabbychat.extra.spell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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
    private LangDict language;
    private SpellDictionary userDict;
    private Set<String> errors = Sets.newHashSet();

    public Spellcheck(File userDict) {
        try {
            if (!userDict.exists()) {
                userDict.createNewFile();
            }
            this.userDict = new SpellDictionaryHashMap(userDict);
            loadLanguage(LangDict.ENGLISH);
        } catch (IOException e) {
            TabbyChat.getLogger().warn("Error whie loading dictionary.", e);
        }
    }

    public void loadLanguage(LangDict lang) throws IOException {
        if (lang == language) {
            return;
        }
        this.language = lang;
        InputStream in = null;
        Reader read = null;
        try {
            in = lang.openStream();
            read = new InputStreamReader(in);
            SpellDictionary dictionary = new SpellDictionaryHashMap(read);
            spellCheck = new SpellChecker(dictionary);
            spellCheck.setUserDictionary(userDict);
            spellCheck.addSpellCheckListener(new SpellCheckListener() {
                @Override
                public synchronized void spellingError(SpellCheckEvent event) {
                    errors.add(event.getInvalidWord());
                }
            });
        } finally {
            Closeables.closeQuietly(in);
            Closeables.closeQuietly(read);
        }
    }

    public LangDict getLanguage() {
        return language;
    }

    public void checkSpelling(String string) {
        this.errors.clear();
        this.spellCheck.checkSpelling(new StringWordTokenizer(string));
    }

    public boolean isCorrect(String word) {
        return !errors.contains(word);
    }

}
