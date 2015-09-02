package mnm.mods.tabbychat.api.gui;

import java.awt.Rectangle;

import mnm.mods.util.gui.GuiComponent;

public interface IGui<Gui extends GuiComponent> {

    /**
     * Gets this gui as its underlying gui type. Convenience method for casting
     * to Gui. Implementation should return this.
     *
     * <pre>
     * &#64;Override
     * public GuiComponent asGui() {
     *     return this;
     * }
     * </pre>
     *
     * @return
     */
    Gui asGui();

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