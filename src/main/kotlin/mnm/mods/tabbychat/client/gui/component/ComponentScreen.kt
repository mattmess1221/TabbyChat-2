package mnm.mods.tabbychat.client.gui.component

import mnm.mods.tabbychat.util.Location
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.text.ITextComponent

/**
 * A panel wrapper for a screen.
 */
open class ComponentScreen(title: ITextComponent) : Screen(title) {

    /**
     * Gets the main panel on this screen. Add things to this.
     *
     * @return The main panel
     */
    protected val panel = GuiPanel()

    override fun render(mouseX: Int, mouseY: Int, tick: Float) {
        panel.render(mouseX, mouseY, tick)
        panel.renderCaption(mouseX, mouseY)
    }

    override fun tick() {
        panel.tick()
    }

    override fun children(): List<IGuiEventListener> {
        return panel.children()
    }

    override fun init(mc: Minecraft, width: Int, height: Int) {
        panel.location = Location(0, 0, width, height)
        panel.clear()
        super.init(mc, width, height)
    }
}
