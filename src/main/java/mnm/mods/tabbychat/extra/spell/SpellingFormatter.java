package mnm.mods.tabbychat.extra.spell;

import java.util.Iterator;

import com.google.common.base.Function;
import com.swabunga.spell.event.SpellCheckEvent;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.ChatBuilder;
import mnm.mods.util.Color;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class SpellingFormatter implements Function<String, IChatComponent> {

    private final Iterator<SpellCheckEvent> spelling;

    private SpellCheckEvent event;
    private int totalLength;

    public SpellingFormatter(Spellcheck sp) {
        spelling = sp.iterator();
    }

    @Override
    public IChatComponent apply(String text) {
        if (!TabbyChat.getInstance().settings.general.spelling.enabled.getValue())
            return new ChatComponentText(text);
        ChatBuilder b = new ChatBuilder();
        int prev = 0;
        while (spelling.hasNext() || event != null) {
            if (event == null)
                event = spelling.next();
            int start = event.getWordContextPosition() - totalLength;
            int end = event.getWordContextPosition() + event.getInvalidWord().length() - totalLength;
            if (end > text.length()) {
                // bail out
                break;
                // I just don't know what went wrong...
            }
            b.text(text.substring(prev, start));
            b.text(text.substring(start, end)).underline(Color.RED);

            prev = end;
            event = null;
        }
        // save where we were at.
        totalLength += prev + 1;
        return b.text(text.substring(prev)).build();
    }

}
