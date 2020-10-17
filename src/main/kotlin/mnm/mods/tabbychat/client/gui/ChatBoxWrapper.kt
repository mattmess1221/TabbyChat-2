package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.screen.Screen
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import kotlin.reflect.KClass

class ChatBoxWrapper(private val type: KClass<out Screen>, private val chatbox: ChatBox) {

    private fun isValid(screen: Screen?) = type.isInstance(screen)

    @SubscribeEvent
    fun initGui(event: GuiScreenEvent.InitGuiEvent.Post) {
        if (isValid(event.gui)) {
            chatbox.init(event.gui)
        }
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (isValid(mc.currentScreen) && event.phase == TickEvent.Phase.END) {
            chatbox.tick()
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderScreen(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = true
            chatbox.render(event.mouseX, event.mouseY, event.renderPartialTicks)
        }
    }

    @SubscribeEvent
    fun onKeyPressed(event: GuiScreenEvent.KeyboardKeyPressedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.keyPressed(event.keyCode, event.scanCode, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onKeyReleased(event: GuiScreenEvent.KeyboardKeyReleasedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.keyReleased(event.keyCode, event.scanCode, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onCharTyped(event: GuiScreenEvent.KeyboardCharTypedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.charTyped(event.codePoint, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onMouseClicked(event: GuiScreenEvent.MouseClickedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.mouseClicked(event.mouseX, event.mouseY, event.button)
        }
    }

    @SubscribeEvent
    fun onMouseReleased(event: GuiScreenEvent.MouseReleasedEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.mouseReleased(event.mouseX, event.mouseY, event.button)
        }
    }

    @SubscribeEvent
    fun onMouseDragged(event: GuiScreenEvent.MouseDragEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.mouseDragged(event.mouseX, event.mouseY, event.mouseButton, event.dragX, event.dragY)
        }
    }

    @SubscribeEvent
    fun onMouseScrolled(event: GuiScreenEvent.MouseScrollEvent.Pre) {
        if (isValid(event.gui)) {
            event.isCanceled = chatbox.mouseScrolled(event.mouseX, event.mouseY, event.scrollDelta)
        }
    }
}