package mnm.mods.tabbychat;

import java.io.File;

import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.core.GuiChatTC;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.core.GuiSleepTC;
import mnm.mods.tabbychat.core.api.TabbyProvider;
import mnm.mods.tabbychat.core.api.TabbyProxy;
import mnm.mods.tabbychat.settings.gui.GuiSettingsGeneral;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.multiplayer.ServerData;

public abstract class TabbyChat extends TabbyAPI {

    private static final LogHelper LOGGER = LogHelper.getLogger(TabbyRef.MOD_ID);
    private static TabbyChat instance;

    private File dataFolder;
    private ServerData currentServer;

    protected static void setInstance(TabbyChat inst) {
        instance = inst;
        setAPI(inst);
    }

    public static TabbyChat getInstance() {
        return instance;
    }

    public static LogHelper getLogger() {
        return LOGGER;
    }

    @Override
    public Chat getChat() {
        // TODO Auto-generated method stub
        return GuiNewChatTC.getInstance().getChatbox();
    }

    @Override
    public void openSettings() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiSettingsGeneral());
    }

    public ServerData getCurrentServer() {
        return this.currentServer;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    // Protected methods

    protected void init() {
        TabbyProvider.getInstance().initProvider();
    }

    protected void setDataDirectory(File config) {
        setDataFolder(new File(config, TabbyRef.MOD_ID));
    }

    protected void onRender(GuiScreen currentScreen) {
        if (currentScreen instanceof GuiChat && !(currentScreen instanceof GuiChatTC)) {
            Minecraft mc = Minecraft.getMinecraft();
            // Get the default text via Access Transforming
            String inputBuffer = ((GuiChat) currentScreen).defaultInputFieldText;
            if (currentScreen instanceof GuiSleepMP)
                mc.displayGuiScreen(new GuiSleepTC());
            else
                mc.displayGuiScreen(new GuiChatTC(inputBuffer));
        }
    }

    protected void onJoin(ServerData serverData) {
        this.currentServer = serverData;
        TabbyProxy.onJoinGame(serverData);
        try {
            hookIntoChat(Minecraft.getMinecraft().ingameGUI);
        } catch (Exception e) {
            LOGGER.fatal("Unable to hook into chat.  This is bad.", e);
        }
    }

    // Private methods

    private void hookIntoChat(GuiIngame guiIngame) {
        if (!GuiNewChatTC.class.isAssignableFrom(guiIngame.getChatGUI().getClass())) {
            guiIngame.persistantChatGUI = GuiNewChatTC.getInstance();
            LOGGER.info("Successfully hooked into chat.");
        }
    }

    private void setDataFolder(File dataFolder) {
        this.dataFolder = dataFolder;
    }
}
