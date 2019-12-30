package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.client.gui.component.layout.ILayout
import mnm.mods.tabbychat.util.Dim
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.INestedGuiEventHandler
import net.minecraft.client.gui.screen.Screen
import kotlin.math.max

/**
 * A component that can contain multiple components.
 *
 * @author Matthew
 */
open class GuiPanel : GuiComponent(), INestedGuiEventHandler {

    private val root: GuiPanel
        get() {
            var child = parent
            while (child?.parent != null) {
                child = child.parent
            }
            return child ?: this
        }

    private val components: MutableList<GuiComponent> = mutableListOf()
    var layout: ILayout? = null

    private var focused2: IGuiEventListener? = null
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
        set(_) {
        }

    override fun render(x: Int, y: Int, parTicks: Float) {
        super.render(x, y, parTicks)

        layout?.layoutComponents(this)
        this.children().asSequence()
                .filter { it.visible }
                .forEach {
                    it.render(x, y, parTicks)
                }

    }

    override fun init(screen: Screen) {
        children().forEach{ it.init(screen) }
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
    fun <T : GuiComponent> add(guiComponent: T, constraints: Any? = null, config: T.() -> Unit = {}): T {
        require(guiComponent.parent == null) { "Component already has a parent." }
        guiComponent.parent = this
        guiComponent.config()
        components.add(guiComponent)
        layout?.addComponent(guiComponent, constraints)
        root.checkForHijacking()
        return guiComponent
    }

    /**
     * Checks all the parents to make sure no-one shares any children or have any duplicates.
     */
    private fun checkForHijacking(list: MutableList<GuiComponent> = mutableListOf()) {
        for (c in children()) {
            require(c !in list) { "YEET: $c is in $list" }
            list.add(c)
            if (c is GuiPanel) {
                c.checkForHijacking(list)
            }
        }
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
        require(guiComp.parent == this) { "Component does not belong to this panel" }
        components.remove(guiComp)
        layout?.removeComponent(guiComp)
        guiComp.parent = null
    }

    override fun children(): List<GuiComponent> {
        return components
    }

    override fun isDragging(): Boolean {
        return this.dragging
    }

    override fun setDragging(p_195072_1_: Boolean) {
        this.dragging = p_195072_1_
    }

    override fun setFocused(focused: IGuiEventListener?) {
        this.focused2 = focused
    }

    override fun getFocused(): IGuiEventListener? = focused2


    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        return super<INestedGuiEventHandler>.mouseClicked(x, y, button)
    }

    override fun mouseReleased(x: Double, y: Double, button: Int): Boolean {
        return super<INestedGuiEventHandler>.mouseReleased(x, y, button)
    }

    override fun mouseDragged(x: Double, y: Double, button: Int, dx: Double, dy: Double): Boolean {
        return super<INestedGuiEventHandler>.mouseDragged(x, y, button, dx, dy)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        return super<INestedGuiEventHandler>.mouseScrolled(x, y, scroll)
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        return super<INestedGuiEventHandler>.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun keyReleased(key: Int, scanCode: Int, modifiers: Int): Boolean {
        return super<INestedGuiEventHandler>.keyReleased(key, scanCode, modifiers)
    }

    override fun charTyped(char: Char, modifiers: Int): Boolean {
        return super<INestedGuiEventHandler>.charTyped(char, modifiers)
    }

    override fun changeFocus(focus: Boolean): Boolean {
        return super<INestedGuiEventHandler>.changeFocus(focus)
    }

    override fun onClosed() {
        this.children().forEach { it.onClosed() }
    }
}
