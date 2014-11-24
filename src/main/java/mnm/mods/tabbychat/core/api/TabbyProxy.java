package mnm.mods.tabbychat.core.api;

import java.net.SocketAddress;
import java.util.List;

import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentFilterEvent;
import mnm.mods.tabbychat.api.listener.events.PostLoginEvent;
import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.GuiButton;

public class TabbyProxy {

    private static TabbyProvider provider = TabbyProvider.getInstance();

    public static String onChatSent(String message) {
        ChatSentEvent sendEvent = new ChatSentEvent(message);
        TabbyProxy.provider.onChatSent(sendEvent);
        ChatSentFilterEvent filter = new ChatSentFilterEvent(message);
        TabbyProxy.provider.onChatSentFilter(filter);
        return filter.message;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void onInitScreen(List<GuiComponent> components) {
        TabbyProxy.provider.onInitScreen(new ChatInitEvent(components));
    }

    public static void onUpdateScreen() {
        TabbyProxy.provider.onUpdateScreen();
    }

    public static void onCloseScreen() {
        TabbyProxy.provider.onCloseScreen();
    }

    public static void onActionPerformed(GuiButton button) {
        TabbyProxy.provider.onActionPerformed(button);
    }

    public static void onJoinGame(SocketAddress address) {
        PostLoginEvent loginEvent = new PostLoginEvent(address);
        TabbyProxy.provider.onPostLogin(loginEvent);
    }

    public static void onChatRecieved(ChatRecievedEvent chat) {
        TabbyProxy.provider.onChatRecieved(chat);
    }

    public static void onChatReicevedFilter(ChatRecievedFilterEvent chat) {
        TabbyProxy.provider.onChatRecievedFilter(chat);
    }
}
