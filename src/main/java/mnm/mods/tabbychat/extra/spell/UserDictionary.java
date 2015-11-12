package mnm.mods.tabbychat.extra.spell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.swabunga.spell.engine.SpellDictionaryHashMap;

/**
 * Dictionary that supports {@code #} comments
 */
public class UserDictionary extends SpellDictionaryHashMap {

    public UserDictionary(File file) throws IOException {
        super(file);
    }

    @Override
    protected void createDictionary(BufferedReader in) throws IOException {
        try {
            for (String line : CharStreams.readLines(in)) {
                if (!line.startsWith("#") && !line.trim().isEmpty()) {
                    putWord(line);
                }
            }
        } finally {
            Closeables.closeQuietly(in);
        }
    }
}
