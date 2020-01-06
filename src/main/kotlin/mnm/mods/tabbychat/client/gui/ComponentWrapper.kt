package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.screen.Screen
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.reflect.KClass

class ComponentWrapper(private val type: KClass<out Screen>, private val panel: GuiComponent) {

    private fun isValid(screen: Screen?) = type.isInstance(screen)

    @SubscribeEvent
    fun initGui(event: GuiScreenEvent.InitGuiEvent.Post) {
        if (isValid(event.gui)) {
            panel.init(event.gui)
        }
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (isValid(mc.currentScreen) && event.phase == TickEvent.Phase.END) {
            panel.tick()
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderScreen(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = true
            panel.render(event.mouseX, event.mouseY, event.renderPartialTicks)
        }
    }

    @SubscribeEvent
    fun onKeyPressed(event: GuiScreenEvent.KeyboardKeyPressedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.keyPressed(event.keyCode, event.scanCode, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onKeyReleased(event: GuiScreenEvent.KeyboardKeyReleasedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.keyReleased(event.keyCode, event.scanCode, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onCharTyped(event: GuiScreenEvent.KeyboardCharTypedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.charTyped(event.codePoint, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onMouseClicked(event: GuiScreenEvent.MouseClickedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.mouseClicked(event.mouseX, event.mouseY, event.button)
        }
    }

    @SubscribeEvent
    fun onMouseReleased(event: GuiScreenEvent.MouseReleasedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.mouseReleased(event.mouseX, event.mouseY, event.button)
        }
    }

    @SubscribeEvent
    fun onMouseDragged(event: GuiScreenEvent.MouseDragEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.mouseDragged(event.mouseX, event.mouseY, event.mouseButton, event.dragX, event.dragY)
        }
    }

    @SubscribeEvent
    fun onMouseScrolled(event: GuiScreenEvent.MouseScrollEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = panel.mouseScrolled(event.mouseX, event.mouseY, event.scrollDelta)
        }
    }
}