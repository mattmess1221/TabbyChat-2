package mnm.mods.tabbychat;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.core.GuiChatTC;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.core.GuiSleepTC;
import mnm.mods.tabbychat.core.api.TabbyProvider;
import mnm.mods.tabbychat.core.api.TabbyProxy;
import mnm.mods.tabbychat.settings.ChannelSettings;
import mnm.mods.tabbychat.settings.ChatBoxSettings;
import mnm.mods.tabbychat.settings.ColorSettings;
import mnm.mods.tabbychat.settings.GeneralSettings;
import mnm.mods.tabbychat.settings.gui.GuiSettingsGeneral;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;

public abstract class TabbyChat extends TabbyAPI {

    private static final LogHelper LOGGER = LogHelper.getLogger(TabbyRef.MOD_ID);
    private static TabbyChat instance;

    public GeneralSettings generalSettings;
    public ChatBoxSettings chatSettings;
    public ColorSettings colorSettings;
    // Server settings
    public ChannelSettings channelSettings;

    private File dataFolder;
    private SocketAddress currentServer;

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

    public SocketAddress getCurrentServer() {
        return this.currentServer;
    }


    public File getDataFolder() {
        return dataFolder;
    }

    // Protected methods

    protected void setConfigFolder(File config) {
        this.dataFolder = new File(config, TabbyRef.MOD_ID);
    }

    protected void init() {
        // Set global settings
        generalSettings = new GeneralSettings();
        chatSettings = new ChatBoxSettings();
        colorSettings = new ColorSettings();

        // Load settings
        generalSettings.loadSettingsFile();
        chatSettings.loadSettingsFile();
        colorSettings.loadSettingsFile();

        // Save settings
        generalSettings.saveSettingsFile();
        chatSettings.saveSettingsFile();
        colorSettings.saveSettingsFile();
        TabbyProvider.getInstance().initProvider();
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

    protected void onJoin(SocketAddress address) {
        this.currentServer = address;
        // Set server settings
        channelSettings = new ChannelSettings((InetSocketAddress) currentServer);
        channelSettings.loadSettingsFile();
        channelSettings.saveSettingsFile();

        try {
            hookIntoChat(Minecraft.getMinecraft().ingameGUI);
        } catch (Exception e) {
            LOGGER.fatal("Unable to hook into chat.  This is bad.", e);
        }
        TabbyProxy.onJoinGame(address);
    }

    // Private methods

    private void hookIntoChat(GuiIngame guiIngame) {
        if (!GuiNewChatTC.class.isAssignableFrom(guiIngame.getChatGUI().getClass())) {
            guiIngame.persistantChatGUI = GuiNewChatTC.getInstance();
            LOGGER.info("Successfully hooked into chat.");
        }
    }

}
