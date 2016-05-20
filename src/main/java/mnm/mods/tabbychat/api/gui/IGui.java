package mnm.mods.tabbychat.api.gui;

import java.awt.Rectangle;

/**
 *
 * @author Matthew
 *
 * @param <Gui> The gui type, such as GuiComponent or GuiPanel
 */
public interface IGui {

    /**
     * Gets the bounds of this gui relative to the parent.
     *
     * @return The size and position
     */
    Rectangle getBounds();

    /**
     * Gets the scale of this gui.
     *
     * @return The scale
     */
    float getScale();
}