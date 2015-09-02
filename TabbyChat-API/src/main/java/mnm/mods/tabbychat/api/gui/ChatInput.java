package mnm.mods.tabbychat.api.gui;

import java.util.List;

import mnm.mods.util.gui.GuiComponent;
import mnm.mods.util.gui.GuiText;

/**
 * The chat input is responsible for storing the chat to be sent before it gets
 * sent in a user friendly way. Seriously.. it's a wrapping text box.
 */
public interface ChatInput extends IGui<GuiComponent> {

    /**
     * Get the contents of the text field wrapped with the width.
     *
     * @return The wrapped lines
     */
    List<String> getWrappedLines();

    /**
     * Get the underlying text field.
     *
     * @return The text field
     */
    GuiText getTextField();
}
