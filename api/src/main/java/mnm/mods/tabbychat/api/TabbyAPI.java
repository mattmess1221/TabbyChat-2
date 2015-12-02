package mnm.mods.tabbychat.api;

import mnm.mods.tabbychat.api.gui.ChatGui;

/**
 * Represents the main object for TabbyChat
 */
public abstract class TabbyAPI {

    private static TabbyAPI instance;

    protected TabbyAPI() {
        instance = this;
    }

    /**
     * Gets the API.
     *
     * @return The api
     */
    public static TabbyAPI getAPI() {
        return instance;
    }

    /**
     * Retrieve TabbyChat's version data. Version data contains the readable
     * version string and comparable double.
     * 
     * @return The version data
     */
    public abstract VersionData getVersionData();

    /**
     * Gets the chat.
     *
     * @return The chat
     */
    public abstract Chat getChat();

    /**
     * Gets the {@link AddonManager} used to register listeners.
     *
     * @return The addon manager
     */
    public abstract AddonManager getAddonManager();

    /**
     * Gets the gui part of the chat box.
     *
     * @return The chat box
     */
    public abstract ChatGui<?> getGui();

}
