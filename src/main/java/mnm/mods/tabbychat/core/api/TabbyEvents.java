package mnm.mods.tabbychat.core.api;

import java.util.List;

import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.listener.ChannelListener;
import mnm.mods.tabbychat.api.listener.ChatInputListener;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.ChatScreenListener;
import mnm.mods.tabbychat.api.listener.ChatScreenRenderer;
import mnm.mods.tabbychat.api.listener.ChatSentListener;
import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentEvent;
import mnm.mods.tabbychat.api.listener.events.MessageAddedToChannelEvent;
import net.minecraft.client.gui.GuiChat;

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

    public void onInitScreen(GuiChat chat) {
        List<ChatScreenListener> listeners = manager.getListenersOfType(ChatScreenListener.class);
        ChatInitEvent init = new ChatInitEvent(chat);
        for (ChatScreenListener screen : listeners) {
            screen.onInitScreen(init);
        }
    }

    public void onRenderChatScreen(int mouseX, int mouseY, float ticks) {
        List<ChatScreenRenderer> renderers = manager.getListenersOfType(ChatScreenRenderer.class);
        for (ChatScreenRenderer render : renderers) {
            render.onRender(mouseX, mouseY, ticks);
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

    public boolean onKeyTyped(char ch, int code) {
        List<ChatInputListener> inputs = manager.getListenersOfType(ChatInputListener.class);
        for (ChatInputListener input : inputs) {
            if (input.onKeyTyped(ch, code)) {
                return true;
            }
        }
        return false;
    }

    public boolean onMouseClicked(int xPos, int yPos, int button) {
        List<ChatInputListener> inputs = manager.getListenersOfType(ChatInputListener.class);
        for (ChatInputListener input : inputs) {
            if (input.onMouseClicked(xPos, yPos, button)) {
                return true;
            }
        }
        return false;
    }
}
