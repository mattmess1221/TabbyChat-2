package mnm.mods.tabbychat.extra.spell;

import com.google.common.base.Function;

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
        spelling.checkSpelling(text);
        String[] split = text.split(" ");
        ChatBuilder b = new ChatBuilder();
        for (String word : split) {
            b.text(word);
            if (!spelling.isCorrect(word)) {
                b.underline(Color.RED);
            }
            b.text(" ");
        }
        return b.build();
    }

}
