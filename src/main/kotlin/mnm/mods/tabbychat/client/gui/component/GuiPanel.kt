package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.client.gui.component.layout.ILayout
import mnm.mods.tabbychat.util.Dim
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.INestedGuiEventHandler
import kotlin.math.max

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
open class GuiPanel() : GuiComponent(), INestedGuiEventHandler {

    private val components: MutableList<GuiComponent> = mutableListOf()
    var layout: ILayout? = null

    private var focused: IGuiEventListener? = null
    private var dragging: Boolean = false

    override var minimumSize: Dim
        get() = layout?.layoutSize ?: run {
            var width = 0
            var height = 0
            for (gc in components) {
                width = max(width, gc.location.width)
                height = max(height, gc.location.height)
            }
            Dim(width, height)
        }
        set(value) {
            super.minimumSize = value
        }

    constructor(layout: ILayout) : this() {
        this.layout = layout
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        layout?.layoutComponents(this)
        this.children().asSequence()
                .filter { it.visible }
                .forEach {
                    it.render(mouseX, mouseY, parTicks)
                }

        super.render(mouseX, mouseY, parTicks)
    }

    override fun renderCaption(x: Int, y: Int) {
        super.renderCaption(x, y)
        this.children().asSequence()
                .filter { it.visible }
                .forEach { it.renderCaption(x, y) }
    }

    override fun tick() {
        children().forEach { it.tick() }
    }

    /**
     * Adds a component to this panel with constraints.
     *
     * @param guiComponent The component
     * @param constraints  The constraints
     */
    fun <T : GuiComponent> add(guiComponent: T, constraints: Any? = null): T {
        guiComponent.parent = this
        components.add(guiComponent)
        layout?.addComponent(guiComponent, constraints)
        return guiComponent
    }

    /**
     * Removes all components from this panel.
     */
    fun clear() {
        components.forEach { comp ->
            comp.parent = null
            layout?.removeComponent(comp)
        }
        components.clear()
    }

    /**
     * Removes a component from this panel.
     *
     * @param guiComp The component to remove
     */
    fun remove(guiComp: GuiComponent) {
        components.remove(guiComp)
        layout?.removeComponent(guiComp)
    }

    override fun children(): List<GuiComponent> {
        return components
    }

    override fun getFocused(): IGuiEventListener? {
        return focused
    }


    override fun setFocused(focused: IGuiEventListener?) {
        this.focused = focused
    }


    override fun isDragging(): Boolean {
        return this.dragging
    }

    override fun setDragging(p_195072_1_: Boolean) {
        this.dragging = p_195072_1_
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return super<INestedGuiEventHandler>.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(x: Double, y: Double, b: Int): Boolean {
        return super<INestedGuiEventHandler>.mouseReleased(x, y, b)
    }

    override fun mouseDragged(x: Double, y: Double, b: Int, dx: Double, dy: Double): Boolean {
        return super<INestedGuiEventHandler>.mouseDragged(x, y, b, dx, dy)
    }

    override fun changeFocus(p_changeFocus_1_: Boolean): Boolean {
        return super<INestedGuiEventHandler>.changeFocus(p_changeFocus_1_)
    }
    override fun onClosed() {
        this.children().forEach { it.onClosed() }
    }
}
