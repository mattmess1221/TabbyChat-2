package mnm.mods.tabbychat.client.extra.spell;

import java.util.Iterator;
import java.util.function.Function;

import com.swabunga.spell.event.SpellCheckEvent;

import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.text.ITextBuilder;
import mnm.mods.tabbychat.util.text.TextBuilder;
import net.minecraft.util.text.ITextComponent;

public class SpellingFormatter implements Function<String, ITextComponent> {

    private final Iterator<SpellCheckEvent> spelling;

    private SpellCheckEvent event;
    private int totalLength;

    public SpellingFormatter(Spellcheck sp) {
        spelling = sp.getErrors().iterator();
    }

    @Override
    public ITextComponent apply(String text) {
        if (text == null)
            return null;
        ITextBuilder b = new TextBuilder();
        int prev = 0;
        int length = totalLength;
        // save where we are at.
        totalLength += text.length();
        while (spelling.hasNext() || event != null) {
            if (event == null)
                event = spelling.next();
            int start = event.getWordContextPosition() - length;
            int end = start + event.getInvalidWord().length();

            if (start < 0) {
                // error started on previous line
                start = prev;
            }
            if (start > text.length()) {
                // no more errors on this line
                break;
            }
            b.text(text.substring(prev, start));

            if (end > text.length()) {
                // error goes to next line
                return b.text(text.substring(start)).underline(Color.RED).build();
            }
            b.text(text.substring(start, end)).underline(Color.RED);

            prev = end;
            event = null;
        }
        // no more errors.
        totalLength++;
        return b.text(text.substring(prev)).build();
    }

}
