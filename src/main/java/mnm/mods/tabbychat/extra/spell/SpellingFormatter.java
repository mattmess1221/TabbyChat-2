package mnm.mods.tabbychat.extra.spell;

import com.google.common.base.Function;
import com.swabunga.spell.event.SpellCheckEvent;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.ChatBuilder;
import mnm.mods.util.Color;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class SpellingFormatter implements Function<String, IChatComponent> {

    private final Spellcheck spelling;

    public SpellingFormatter(Spellcheck sp) {
        spelling = sp;
    }

    @Override
    public IChatComponent apply(String text) {
        if (!TabbyChat.getInstance().settings.general.spelling.enabled.getValue())
            return new ChatComponentText(text);
        ChatBuilder b = new ChatBuilder();
        int prev = 0;
        for (SpellCheckEvent event : spelling) {
            int start = event.getWordContextPosition();
            int end = event.getWordContextPosition() + event.getInvalidWord().length();

            b.text(text.substring(prev, start));
            b.text(text.substring(start, end)).underline(Color.RED);

            prev = end;
        }
        return b.text(text.substring(prev)).build();
    }

}
