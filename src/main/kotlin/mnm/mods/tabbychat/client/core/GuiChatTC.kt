package mnm.mods.tabbychat.client.core

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.ChatBox
import mnm.mods.tabbychat.client.gui.suggestionHelper
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import org.lwjgl.glfw.GLFW

object GuiChatTC {

    @SubscribeEvent
    fun initGui(event: GuiScreenEvent.InitGuiEvent.Post) {
        if (event.gui is ChatScreen) {
            val guichat = event.gui as ChatScreen
            val chan = ChatBox.activeChannel
            if (guichat.defaultInputFieldText.isEmpty()
                    && !chan.isPrefixHidden
                    && chan.prefix.isNotEmpty()) {
                guichat.defaultInputFieldText = chan.prefix + " "
            }
            val text = ChatBox.chatInput.textField
            guichat.inputField = text.delegate
            text.value = guichat.defaultInputFieldText

            ChatBox.chatInput.textFormatter = guichat.suggestionHelper::func_228122_a_
            text.delegate.setResponder { guichat.func_212997_a(it) }

            val children = guichat.children() as MutableList<IGuiEventListener>
            children[0] = ChatBox
        }
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (mc.currentScreen is ChatScreen && event.phase == TickEvent.Phase.END) {
            ChatBox.tick()
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onRenderChat(event: GuiScreenEvent.DrawScreenEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = true
            ChatBox.update(event.gui as ChatScreen)
            ChatBox.render(event.mouseX, event.mouseY, event.renderPartialTicks)
        }
    }

    @SubscribeEvent
    fun onKeyPressed(event: GuiScreenEvent.KeyboardKeyPressedEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = keyPressed(event.gui as ChatScreen, event.keyCode) || ChatBox.keyPressed(event.keyCode, event.scanCode, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onKeyReleased(event: GuiScreenEvent.KeyboardKeyReleasedEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = ChatBox.keyReleased(event.keyCode, event.scanCode, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onCharTyped(event: GuiScreenEvent.KeyboardCharTypedEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = ChatBox.charTyped(event.codePoint, event.modifiers)
        }
    }

    @SubscribeEvent
    fun onMouseClicked(event: GuiScreenEvent.MouseClickedEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = ChatBox.mouseClicked(event.mouseX, event.mouseY, event.button)
        }
    }

    @SubscribeEvent
    fun onMouseReleased(event: GuiScreenEvent.MouseReleasedEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = ChatBox.mouseReleased(event.mouseX, event.mouseY, event.button)
        }
    }

    @SubscribeEvent
    fun onMouseDragged(event: GuiScreenEvent.MouseDragEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = ChatBox.mouseDragged(event.mouseX, event.mouseY, event.mouseButton, event.dragX, event.dragY)
        }
    }

    @SubscribeEvent
    fun onMouseScrolled(event: GuiScreenEvent.MouseScrollEvent.Pre) {
        if (event.gui is ChatScreen) {
            event.isCanceled = ChatBox.mouseScrolled(event.mouseX, event.mouseY, event.scrollDelta)
        }
    }

    private fun keyPressed(guichat: ChatScreen, key: Int): Boolean {
        if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            mc.ingameGUI.chatGUI.resetScroll()
            val text = ChatBox.chatInput.textField
            guichat.sendMessage(text.value)
            text.value = guichat.defaultInputFieldText

            if (!TabbyChatClient.settings.advanced.keepChatOpen.value) {
                mc.displayGuiScreen(null)
            }
            return true
        }
        return false
    }
}