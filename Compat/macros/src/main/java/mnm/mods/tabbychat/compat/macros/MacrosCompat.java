package mnm.mods.tabbychat.compat.macros;

import com.google.common.eventbus.Subscribe;
import com.mumfrey.liteloader.transformers.event.EventInfo;

import mnm.mods.tabbychat.api.events.InputEvent;
import mnm.mods.tabbychat.api.events.ScreenEvent;
import net.eq2online.macros.core.MacroModCore;
import net.minecraft.client.gui.GuiChat;

public class MacrosCompat {

    @Subscribe
    public void onRender(ScreenEvent.Render event) {
        // draw buttons
        MacroModCore.preChatGuiEvent(mkEvnt("", event.chatScreen, false), event.mouseX, event.mouseY, event.ticks);
        // draw wrench (gui editor)
        MacroModCore.onChatGuiEvent(mkEvnt("", event.chatScreen, false), event.mouseX, event.mouseY, event.ticks);
    }

    @Subscribe
    public void onMouseClicked(InputEvent.MouseClick mouse) {
        EventInfo<GuiChat> event = mkEvnt("", mouse.chatScreen, true);
        MacroModCore.onChatGuiEvent(event, mouse.mouseX, mouse.mouseY, mouse.button);
        if (event.isCancelled()) {
            mouse.setCancelled(true);
        }
    }

    @Subscribe
    public void onUpdateScreen(ScreenEvent.Update event) {
        MacroModCore.onChatGuiEvent(mkEvnt("", event.chatScreen, false));
    }

    private static <T> EventInfo<T> mkEvnt(String s, T t, boolean c) {
        return new EventInfo<T>(s, t, c);
    }
}
