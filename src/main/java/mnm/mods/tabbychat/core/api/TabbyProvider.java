package mnm.mods.tabbychat.core.api;

import java.util.LinkedHashMap;
import java.util.List;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.listener.ChatRecievedListener;
import mnm.mods.tabbychat.api.listener.ChatScreenListener;
import mnm.mods.tabbychat.api.listener.ChatSentListener;
import mnm.mods.tabbychat.api.listener.TabbyAddon;
import mnm.mods.tabbychat.api.listener.events.AddonInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatInitEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatRecievedFilterEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentEvent;
import mnm.mods.tabbychat.api.listener.events.ChatMessageEvent.ChatSentFilterEvent;
import mnm.mods.tabbychat.api.listener.events.PostLoginEvent;
import mnm.mods.util.LogHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.launchwrapper.Launch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TabbyProvider {

    private static LogHelper logger = TabbyChat.getLogger();
    private static TabbyProvider instance;

    private LinkedHashMap<String, TabbyAddon> addonsList = Maps.newLinkedHashMap();

    private TabbyProvider() {
    }

    public static TabbyProvider getInstance() {
        if (instance == null)
            instance = new TabbyProvider();
        return instance;
    }

    /**
     * Gets whether an addon is installed or not.
     *
     * @param moduleName The fully qualified name of the class
     * @return Whether the addon is installed
     */
    public boolean isAddonInstalled(String moduleName) {
        return addonsList.containsKey(moduleName);
    }

    public void initProvider() {
        TabbyEnumerator.detectAddonsOnClasspath(Launch.classLoader);
    }

    public void addBaseListener(TabbyAddon chatBase) {
        logger.info("Initializing module " + chatBase.getClass().getName());
        try {
            if (!this.addonsList.containsValue(chatBase)) {
                initAddon(chatBase);
            }
        } catch (AddonException me) {
            logger.debug("Unable to load module " + chatBase.getClass().getName());
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends TabbyAddon> List<T> getListenersOfType(Class<T> type) {
        List<T> list = Lists.newArrayList();
        for (TabbyAddon chatBase : this.addonsList.values()) {
            if (type.isAssignableFrom(chatBase.getClass()))
                list.add((T) chatBase);
        }
        return list;
    }

    void initAddon(TabbyAddon addon) throws AddonException {
        try {
            AddonInitEvent event = new AddonInitEvent();
            addon.initAddon(event);
            if (event.shouldLoad) {
                this.addonsList.put(addon.getClass().getName(), addon);
            }
        } catch (Throwable t) {
            throw new AddonException("Error while loading " + addon.getClass().getName(), t);
        }
    }

    void onInitScreen(ChatInitEvent chatInitEvent) {
        for (ChatScreenListener addon : this.getListenersOfType(ChatScreenListener.class))
            addon.onInitScreen(chatInitEvent);
    }

    void onUpdateScreen() {
        for (ChatScreenListener addon : this.getListenersOfType(ChatScreenListener.class))
            addon.onUpdateScreen();
    }

    void onCloseScreen() {
        for (ChatScreenListener addon : this.getListenersOfType(ChatScreenListener.class))
            addon.onCloseScreen();
    }

    void onActionPerformed(GuiButton button) {
        for (ChatScreenListener addon : this.getListenersOfType(ChatScreenListener.class))
            addon.actionPreformed(button);
    }

    void onChatSent(ChatSentEvent sendEvent) {
        for (ChatSentListener addon : this.getListenersOfType(ChatSentListener.class))
            addon.onChatSent(sendEvent);
    }

    public void onChatSentFilter(ChatSentFilterEvent filter) {
        for (ChatSentListener addon : this.getListenersOfType(ChatSentListener.class)) {
            addon.onChatSentFilter(filter);
        }
    }

    void onChatRecieved(ChatRecievedEvent chat) {
        for (ChatRecievedListener addon : this.getListenersOfType(ChatRecievedListener.class))
            addon.onChatRecieved(chat);
    }

    void onChatRecievedFilter(ChatRecievedFilterEvent chat) {
        for (ChatRecievedListener addon : this.getListenersOfType(ChatRecievedListener.class))
            addon.onChatRecievedFilter(chat);
    }

    void onPostLogin(PostLoginEvent loginEvent) {
        for (JoinGameListener addon : this.getListenersOfType(JoinGameListener.class))
            addon.onJoinGame(loginEvent);
    }

}
