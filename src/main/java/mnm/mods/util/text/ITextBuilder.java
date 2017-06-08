package mnm.mods.util.text;

import mnm.mods.util.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;

public interface ITextBuilder {

    /**
     * Sets the formatting in the style of the current chat.
     *
     * @param f The format
     * @return This builder
     */
    ITextBuilder format(TextFormatting f);

    ITextBuilder color(Color color);

    ITextBuilder underline(Color color);

    ITextBuilder highlight(Color color);

    ITextBuilder click(ClickEvent event);

    ITextBuilder hover(HoverEvent event);

    ITextBuilder insertion(String insertion);

    ITextBuilder score(String player, String objective);

    ITextBuilder text(String text);

    ITextBuilder selector(Selector selector);

    /**
     * Starts the creation of a translation. After calling, any call to
     * {@link #append(ITextComponent)} won't be immediately appended to the
     * component. Instead, it will be added to a list of arguments for the
     * translation. To end the translation and allow it to be appended to the
     * chat, call {@link #end()}.
     *
     * @param key The translation key
     * @return A translation builder
     */
    ITextBuilder translation(String key);

    /**
     * Quickly translates the given key with zero arguments or formatting.
     * Immidiently ends the translation. Same as calling
     * {@code translation(key).end()}
     *
     * @param key
     * @return
     */
    ITextBuilder quickTranslate(String key);

    /**
     * Used for builders that build multiple chats.
     *
     * @return This builder
     */
    ITextBuilder next();

    /**
     * Ends a translation so it can be appended to the chat.
     *
     * @return
     */
    ITextBuilder end();

    /**
     * Appends the current chat to the chat and makes the provided value the
     * current.
     *
     * @param chat The new current value
     * @return
     */
    ITextBuilder append(@Nullable ITextComponent chat);

    /**
     * Appends the current chat (if any) to the chat and returns the built chat.
     *
     * @return The chat
     */
    ITextComponent build();

}
