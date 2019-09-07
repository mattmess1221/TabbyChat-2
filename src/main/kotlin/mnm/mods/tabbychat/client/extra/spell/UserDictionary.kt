package mnm.mods.tabbychat.client.extra.spell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;

import com.swabunga.spell.engine.SpellDictionaryHashMap;

/**
 * Dictionary that supports {@code #} comments
 */
public class UserDictionary extends SpellDictionaryHashMap {

    UserDictionary(Reader reader) throws IOException {
        super(reader);
    }

    @Override
    protected void createDictionary(BufferedReader in) throws IOException {
        try {
            in.lines()
                    .filter(line -> line.startsWith("#") && !line.trim().isEmpty())
                    .forEach(this::putWord);
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }
}
