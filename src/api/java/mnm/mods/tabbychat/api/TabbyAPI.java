package mnm.mods.tabbychat.api;

import mnm.mods.util.gui.SettingPanel;

/**
 * Represents the main object for TabbyChat
 */
public abstract class TabbyAPI {

    private static TabbyAPI instance;

    protected static void setAPI(TabbyAPI tabbychat) {
        if (instance != null) {
            throw new RuntimeException("TabbyChat 2 API is already initialized!");
        }
        instance = tabbychat;
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
     * Registers a setting category with the setting screen.
     * 
     * @param setting The setting class.
     */
    public abstract void registerSettings(Class<? extends SettingPanel> setting);

    /**
     * Gets the chat.
     *
     * @return The chat
     */
    public abstract Chat getChat();

}
