package mnm.mods.util.text;

import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class TextBuilder extends AbstractChatBuilder {

    private ITextComponent chat;

    /**
     * Used for builders that build multiple chats.
     *
     * @return This builder
     */
    @Override
    public ITextBuilder next() {
        throw new UnsupportedOperationException();
    }

    /**
     * Ends a translation so it can be appended to the chat.
     *
     * @return
     */
    @Override
    public ITextBuilder end() {
        throw new UnsupportedOperationException();
    }

    /**
     * Appends the current chat to the chat and makes the provided value the
     * current.
     *
     * @param chat The new current value
     * @return
     */
    @Override
    public TextBuilder append(@Nullable ITextComponent chat) {

        if (current != null) {
            if (this.chat == null)
                this.chat = current;
            else
                this.chat.appendSibling(current);
        }
        current = chat;
        return this;
    }

    /**
     * Appends the current chat (if any) to the chat and returns the built chat.
     *
     * @return The chat
     */
    @Override
    public ITextComponent build() {
        return append(null).chat;
    }
}
