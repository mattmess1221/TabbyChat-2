package mnm.mods.tabbychat.client.core;

import mnm.mods.tabbychat.client.AbstractChannel;
import mnm.mods.tabbychat.client.TabbyChatClient;
import mnm.mods.tabbychat.client.gui.ChatBox;
import mnm.mods.tabbychat.client.gui.component.GuiText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class GuiChatTC {

    private final ChatBox chat;

    public GuiChatTC(ChatBox chat) {
        this.chat = chat;
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void initGui(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.getGui() instanceof GuiChat) {
            GuiChat guichat = (GuiChat) event.getGui();
            AbstractChannel chan = chat.getActiveChannel();
            if (guichat.defaultInputFieldText.isEmpty()
                    && !chan.isPrefixHidden()
                    && !chan.getPrefix().isEmpty()) {
                guichat.defaultInputFieldText = chan.getPrefix() + " ";
            }
            GuiText text = chat.getChatInput().getTextField();
            guichat.inputField = text.getTextField();
            text.setValue(guichat.defaultInputFieldText);

            text.getTextField().setTextFormatter(guichat::formatMessage);
            text.getTextField().setTextAcceptHandler(guichat::acceptMessage);

            List<IGuiEventListener> children = (List<IGuiEventListener>) guichat.getChildren();
            children.set(0, chat);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().currentScreen instanceof GuiChat && event.phase == TickEvent.Phase.END) {
            chat.tick();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRenderChat(GuiScreenEvent.DrawScreenEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            event.setCanceled(true);
            this.chat.render(event.getMouseX(), event.getMouseY(), event.getRenderPartialTicks());
        }
    }

    @SubscribeEvent
    public void onKeyPressed(GuiScreenEvent.KeyboardKeyPressedEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (keyPressed((GuiChat) event.getGui(), event.getKeyCode())
                    || this.chat.keyPressed(event.getKeyCode(), event.getScanCode(), event.getModifiers())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onKeyReleased(GuiScreenEvent.KeyboardKeyReleasedEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (this.chat.keyPressed(event.getKeyCode(), event.getScanCode(), event.getModifiers())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onCharTyped(GuiScreenEvent.KeyboardCharTypedEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (this.chat.charTyped(event.getCodePoint(), event.getModifiers())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseClickedEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (this.chat.mouseClicked(event.getMouseX(), event.getMouseY(), event.getButton())) {
                event.setCanceled(true);
            }
        }
    }
    @SubscribeEvent
    public void onMouseReleased(GuiScreenEvent.MouseReleasedEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (this.chat.mouseReleased(event.getMouseX(), event.getMouseY(), event.getButton())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onMouseDragged(GuiScreenEvent.MouseDragEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (this.chat.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY())) {
                event.setCanceled(true);
            }
        }
    }


    @SubscribeEvent
    public void onMouseScrolled(GuiScreenEvent.MouseScrollEvent.Pre event) {
        if (event.getGui() instanceof GuiChat) {
            if (this.chat.mouseScrolled(event.getScrollDelta())) {
                event.setCanceled(true);
            }
        }
    }

    private boolean keyPressed(GuiChat guichat, int key) {
        if (key == GLFW.GLFW_KEY_ENTER || key == GLFW.GLFW_KEY_KP_ENTER) {
            Minecraft.getInstance().ingameGUI.getChatGUI().resetScroll();
            GuiText text = this.chat.getChatInput().getTextField();
            guichat.sendChatMessage(text.getValue());
            text.setValue(guichat.defaultInputFieldText);

            if (!TabbyChatClient.getInstance().getSettings().advanced.keepChatOpen.get()) {
                Minecraft.getInstance().displayGuiScreen(null);
            }
            return true;
        }
        return false;
    }
}
