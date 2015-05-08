package mnm.mods.tabbychat.core.api;

import java.util.List;

import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.listener.ChannelListener;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.ChatScreenListener;
import mnm.mods.tabbychat.api.listener.ChatSentListener;
import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentEvent;
import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;
import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.GuiButton;

public class TabbyEvents {

    private final AddonManager manager;

    public TabbyEvents(AddonManager manager) {
        this.manager = manager;
    }

    public String onChatSent(String message) {
        List<ChatSentListener> listeners = manager.getListenersOfType(ChatSentListener.class);
        ChatSentEvent sendEvent = new ChatSentEvent(message);
        for (ChatSentListener listener : listeners) {
            listener.onChatSent(sendEvent);
        }
        return sendEvent.message;
    }

    public void onInitScreen(List<GuiComponent> components) {
        List<ChatScreenListener> listeners = manager.getListenersOfType(ChatScreenListener.class);
        ChatInitEvent init = new ChatInitEvent(components);
        for (ChatScreenListener screen : listeners) {
            screen.onInitScreen(init);
        }
    }

    public void onUpdateScreen() {
        List<ChatScreenListener> listeners = manager.getListenersOfType(ChatScreenListener.class);
        for (ChatScreenListener screen : listeners) {
            screen.onUpdateScreen();
        }
    }

    public void onCloseScreen() {
        List<ChatScreenListener> listeners = manager.getListenersOfType(ChatScreenListener.class);
        for (ChatScreenListener screen : listeners) {
            screen.onCloseScreen();
        }
    }

    public void onActionPerformed(GuiButton button) {
        List<ChatScreenListener> listeners = manager.getListenersOfType(ChatScreenListener.class);
        for (ChatScreenListener screen : listeners) {
            screen.actionPreformed(button);
        }
    }

    public void onChatRecieved(ChatRecievedEvent chat) {
        List<ChatRecievedListener> listeners = manager
                .getListenersOfType(ChatRecievedListener.class);
        for (ChatRecievedListener chatrecieve : listeners) {
            chatrecieve.onChatRecieved(chat);
        }
    }

    public void onMessageAddedToChannel(MessageAddedToChannelEvent event) {
        List<ChannelListener> listeners = manager.getListenersOfType(ChannelListener.class);
        for (ChannelListener chan : listeners) {
            chan.onMessageAdded(event);
        }
    }
}
