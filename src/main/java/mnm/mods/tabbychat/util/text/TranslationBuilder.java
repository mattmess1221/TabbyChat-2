package mnm.mods.tabbychat.util.text;

import com.google.common.collect.Lists;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import javax.annotation.Nullable;

class TranslationBuilder extends AbstractChatBuilder {

    private final ITextBuilder parent;
    private final String translationKey;
    private List<Object> translationArgs = Lists.newArrayList();

    private ITextComponent buffer;

    TranslationBuilder(ITextBuilder parent, String key) {
        this.parent = parent;
        this.translationKey = key;
    }

    @Override
    public ITextBuilder next() {
        translationArgs.add(append(null).buffer);
        buffer = null;
        return this;
    }

    @Override
    public ITextBuilder end() {
        ITextComponent buffer = append(null).buffer;
        if (buffer != null)
            translationArgs.add(buffer);
        return parent.append(new TranslationTextComponent(translationKey, translationArgs.toArray()));
    }

    @Override
    public ITextComponent build() {
        throw new IllegalStateException("Translation in progress.");
    }

    @Override
    public TranslationBuilder append(@Nullable ITextComponent chat) {
        if (current != null) {
            if (this.buffer == null)
                buffer = current;
            else
                this.buffer.appendSibling(current);
        }
        current = chat;
        return this;
    }
}
