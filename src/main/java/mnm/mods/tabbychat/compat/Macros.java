package mnm.mods.tabbychat.compat;

import com.google.common.eventbus.Subscribe;
import com.mumfrey.liteloader.transformers.event.EventInfo;

import mnm.mods.tabbychat.api.events.ChatScreenEvents.ChatUpdateEvent;
import mnm.mods.tabbychat.api.internal.Compat;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiRenderEvent;
import net.eq2online.macros.core.MacroModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;

@Compat("macros")
public class Macros {

    @Subscribe
    public void onRender(GuiRenderEvent render) {
        GuiChat chat = getChat();
        if (chat == null)
            return;
        // draw buttons
        MacroModCore.preChatGuiEvent(mkEvnt("", chat, false), render.getMouseX(), render.getMouseY(), render.getTicks());
        // draw wrench (gui editor)
        MacroModCore.onChatGuiEvent(mkEvnt("", chat, false), render.getMouseX(), render.getMouseY(), render.getTicks());
    }

    @Subscribe
    public void onMouseClicked(GuiMouseEvent mouse) {
        GuiChat chat = getChat();
        if (chat == null)
            return;
        EventInfo<GuiChat> event = mkEvnt("", chat, true);
        MacroModCore.onChatGuiEvent(event, mouse.getMouseX(), mouse.getMouseY(), mouse.getButton());
        // TODO make cancellable
    }

    @Subscribe
    public void onUpdateScreen(ChatUpdateEvent event) {
        GuiChat chat = getChat();
        if (chat == null)
            return;
        MacroModCore.onChatGuiEvent(mkEvnt("", chat, false));
    }

    private static <T> EventInfo<T> mkEvnt(String s, T t, boolean c) {
        return new EventInfo<T>(s, t, c);
    }

    private static GuiChat getChat() {
        GuiScreen sc = Minecraft.getMinecraft().currentScreen;
        if (sc instanceof GuiChat) {
            return (GuiChat) sc;
        }
        return null;
    }
}
