package mnm.mods.tabbychat.api;

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
     * Opens the general settings screen.
     */
    public abstract void openSettings();

    /**
     * Gets the chat.
     *
     * @return The chat
     */
    public abstract Chat getChat();

}
