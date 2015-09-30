package mnm.mods.tabbychat.compat.macros;

import com.mumfrey.liteloader.transformers.event.EventInfo;

import mnm.mods.tabbychat.api.listener.ChatInputListener;
import mnm.mods.tabbychat.api.listener.ChatScreenListener;
import mnm.mods.tabbychat.api.listener.ChatScreenRenderer;
import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import net.eq2online.macros.core.MacroModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;

public class MacrosCompat implements ChatScreenListener, ChatScreenRenderer, ChatInputListener {

    @Override
    public void onRender(int mouseX, int mouseY, float parTick) {
        GuiChat chat = getChat();
        if (chat == null)
            return;
        // draw buttons
        MacroModCore.preChatGuiEvent(mkEvnt("", chat, false), mouseX, mouseY, parTick);
        // draw wrench (gui editor)
        MacroModCore.onChatGuiEvent(mkEvnt("", chat, false), mouseX, mouseY, parTick);
    }

    @Override
    public void onInitScreen(ChatInitEvent chatInitEvent) {}

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int button) {
        GuiChat chat = getChat();
        if (chat == null)
            return false;
        EventInfo<GuiChat> event = mkEvnt("", chat, true);
        MacroModCore.onChatGuiEvent(event, mouseX, mouseY, button);
        return event.isCancelled();
    }

    @Override
    public boolean onKeyTyped(char ch, int code) {
        return false;
    }

    @Override
    public void onUpdateScreen() {
        GuiChat chat = getChat();
        if (chat == null)
            return;
        MacroModCore.onChatGuiEvent(mkEvnt("", chat, false));
    }

    @Override
    public void onCloseScreen() {}

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
