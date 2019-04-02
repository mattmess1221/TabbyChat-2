package mnm.mods.tabbychat.client.extra.spell;

import com.google.common.collect.Lists;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import mnm.mods.tabbychat.TabbyChat;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResourceManager;
import net.minecraft.client.resources.Language;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.resource.VanillaResourceType;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Spellcheck implements ISelectiveResourceReloadListener {

    private final Path userFile;

    private SpellChecker spellCheck;
    private SpellDictionary userDict;
    private List<SpellCheckEvent> errors = Lists.newArrayList();

    public Spellcheck(Path configDir) {
        userFile = configDir.resolve("userdict.txt");
        this.loadCurrentLanguage();
    }

    public synchronized void loadDictionary(LangDict lang) throws IOException {
        try (Reader read = new InputStreamReader(openLangStream(lang))) {
            SpellDictionary dictionary = new SpellDictionaryHashMap(read);
            spellCheck = new SpellChecker(dictionary);
            spellCheck.setUserDictionary(userDict);
            spellCheck.addSpellCheckListener(errors::add);
        }
    }

    private InputStream openLangStream(LangDict lang) throws IOException {
        try {
            return lang.openStream();
        } catch (FileNotFoundException e) {
            if (lang == LangDict.ENGLISH) {
                // Prevent StackOverflowException
                throw e;
            }
            TabbyChat.logger.warn(e + " Falling back to English.");
            return openLangStream(LangDict.ENGLISH);
        }
    }

    public synchronized void loadUserDictionary() throws IOException {
        if (Files.notExists(userFile)) {
            Files.createDirectories(userFile.getParent());
            Files.createFile(userFile);
            try (BufferedWriter w = Files.newBufferedWriter(userFile)) {
                w.write("# User dictionary, one entry per line.");
            }
        }
        try (Reader r = Files.newBufferedReader(userFile)) {
            this.userDict = new UserDictionary(r);

            // set it if it has been created yet.
            if (this.spellCheck != null) {
                this.spellCheck.setUserDictionary(this.userDict);
            }
        }
    }

    public synchronized void addToDictionary(String word) {
        // add to user dictionary
        this.userDict.addWord(word);
    }

    public Path getUserDictionaryFile() {
        return userFile;
    }

    public Iterable<SpellCheckEvent> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    public void checkSpelling(String string) {
        if (spellCheck != null) {
            this.errors.clear();
            this.spellCheck.checkSpelling(new StringWordTokenizer(string));
        }
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            loadCurrentLanguage();
        }
    }

    private void loadCurrentLanguage() {
        Language lang = Minecraft.getInstance().getLanguageManager().getCurrentLanguage();
        try {
            loadUserDictionary();
            loadDictionary(LangDict.fromLanguage(lang.getLanguageCode()));
        } catch (IOException e) {
            TabbyChat.logger.warn("Error while loading dictionary {}.", lang.getLanguageCode(), e);
        }
    }

}
