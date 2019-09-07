package mnm.mods.tabbychat.client.gui.component.layout

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.Dim

/**
 * A layout is used on a [GuiPanel] to easily arrange it's components in a
 * certain way.
 */
interface ILayout {

    /**
     * Gets the estimated size of this layout.
     *
     * @return The size.
     */
    val layoutSize: Dim

    /**
     * Tells this layout that a component was added.
     *
     * @param comp The component
     * @param constraints The optional constraints, may be null
     */
    fun addComponent(comp: GuiComponent, constraints: Any?)

    /**
     * Tells this layout that a component was removed.
     *
     * @param comp The component
     */
    fun removeComponent(comp: GuiComponent)

    /**
     * Called to layout the components. Called before the panel is drawn.
     */
    fun layoutComponents(parent: GuiPanel)

}
