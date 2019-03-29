package mnm.mods.util.gui;

import mnm.mods.util.Dim;

/**
 * A layout is used on a {@link GuiPanel} to easily arrange it's components in a
 * certain way.
 */
public interface ILayout {

    /**
     * Tells this layout that a component was added.
     *
     * @param comp The component
     * @param constraints The optional constraints, may be null
     */
    void addComponent(GuiComponent comp, Object constraints);

    /**
     * Tells this layout that a component was removed.
     *
     * @param comp The component
     */
    void removeComponent(GuiComponent comp);

    /**
     * Called to layout the components. Called before the panel is drawn.
     */
    void layoutComponents(GuiPanel parent);

    /**
     * Gets the estimated size of this layout.
     *
     * @return The size.
     */
    Dim getLayoutSize();

}
