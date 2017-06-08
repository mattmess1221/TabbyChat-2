package mnm.mods.util.gui;

/**
 * An input for guis. Provides a getter and setter
 *
 * @author Matthew
 * @param <T> The input type
 */
public interface IGuiInput<T> {

    /**
     * Gets the value.
     *
     * @return The value
     */
    T getValue();

    /**
     * Sets the value
     *
     * @param value The value
     */
    void setValue(T value);
}
