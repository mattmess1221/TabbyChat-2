package mnm.mods.tabbychat.extra.spell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;

import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.Language;

public class Spellcheck implements Iterable<SpellCheckEvent>, IResourceManagerReloadListener {

    private final File userFile;

    private SpellChecker spellCheck;
    private LangDict language;
    private SpellDictionary userDict;
    private List<SpellCheckEvent> errors = Lists.newArrayList();

    public Spellcheck(File configDir) {
        userFile = new File(configDir, "userdict.txt");
        this.loadCurrentLanguage();
    }

    public synchronized void loadDictionary(LangDict lang) throws IOException {
        if (lang == null || lang.equals(language)) {
            return;
        }
        InputStream in = null;
        Reader read = null;
        try {
            try {
                in = lang.openStream();
            } catch (FileNotFoundException e) {
                TabbyChat.getLogger().warn(e + " Falling back to English.");
                lang = LangDict.ENGLISH;
                in = lang.openStream();
            }
            read = new InputStreamReader(in);
            SpellDictionary dictionary = new SpellDictionaryHashMap(read);
            spellCheck = new SpellChecker(dictionary);
            spellCheck.setUserDictionary(userDict);
            spellCheck.addSpellCheckListener(errors::add);
            this.language = lang;
        } finally {
            Closeables.closeQuietly(in);
            Closeables.closeQuietly(read);
        }
    }

    public synchronized void loadUserDictionary() throws IOException {
        if (!userFile.exists()) {
            userFile.getParentFile().mkdirs();
            userFile.createNewFile();
            Files.write("# User dictionary, one entry per line.", userFile, Charsets.UTF_8);
        }
        this.userDict = new UserDictionary(userFile);
        // set it if it has been created yet.
        if (this.spellCheck != null) {
            this.spellCheck.setUserDictionary(this.userDict);
        }
    }

    public synchronized void addToDictionary(String word) {
        // add to user dictionary
        this.userDict.addWord(word);
    }

    public LangDict getLanguage() {
        return language;
    }

    public File getUserDictionaryFile() {
        return userFile;
    }

    public void checkSpelling(String string) {
        if (spellCheck != null) {
            this.errors.clear();
            this.spellCheck.checkSpelling(new StringWordTokenizer(string));
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        loadCurrentLanguage();
    }

    private void loadCurrentLanguage() {
        Language lang = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage();
        try {
            loadUserDictionary();
            loadDictionary(LangDict.fromLanguage(lang));
        } catch (IOException e) {
            TabbyChat.getLogger().warn("Error while loading dictionary.", e);
        }
    }

    @Override
    public Iterator<SpellCheckEvent> iterator() {
        return ImmutableList.copyOf(errors).iterator();
    }

}
