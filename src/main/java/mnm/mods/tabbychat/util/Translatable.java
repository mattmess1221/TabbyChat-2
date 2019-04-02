package mnm.mods.tabbychat.util;

import net.minecraft.client.resources.I18n;

/**
 * Translatable strings. Translations are done via
 * {@link I18n#format(String, Object...)}.
 *
 * @author Matthew
 */
public interface Translatable {

    /**
     * Gets the unlocalized string for this translation
     *
     * @return The untranslated string
     */
    String getUnlocalized();

    /**
     * Translates this string.
     *
     * </pre>
     *
     * @param params Translation parameters
     * @return The translated string
     */
    default String translate(Object... params) {
        return I18n.format(getUnlocalized(), params);
    }
}
