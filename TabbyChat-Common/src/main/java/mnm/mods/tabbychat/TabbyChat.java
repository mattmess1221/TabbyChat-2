package mnm.mods.tabbychat;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.VersionData;
import mnm.mods.tabbychat.api.filters.FilterVariable;
import mnm.mods.tabbychat.api.internal.ForgeProxy;
import mnm.mods.tabbychat.api.internal.InternalAPI;
import mnm.mods.tabbychat.core.GuiChatTC;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.core.GuiSleepTC;
import mnm.mods.tabbychat.core.api.TabbyAddonManager;
import mnm.mods.tabbychat.core.api.TabbyEvents;
import mnm.mods.tabbychat.extra.ChatAddonAntiSpam;
import mnm.mods.tabbychat.extra.ChatLogging;
import mnm.mods.tabbychat.extra.filters.FilterAddon;
import mnm.mods.tabbychat.extra.spell.Spellcheck;
import mnm.mods.tabbychat.gui.ChatBox;
import mnm.mods.tabbychat.gui.settings.GuiSettingsScreen;
import mnm.mods.tabbychat.liteloader.TabbyPrivateFields;
import mnm.mods.tabbychat.settings.ServerSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.DefaultForgeProxy;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.MnmUtils;
import mnm.mods.util.gui.config.SettingPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.player.EntityPlayer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class TabbyChat extends TabbyAPI implements InternalAPI {

    private static final Logger LOGGER = LogManager.getLogger(TabbyRef.MOD_ID);

    private AddonManager addonManager;
    private TabbyEvents events;
    private Spellcheck spellcheck;
    private ForgeProxy forgeProxy = new DefaultForgeProxy();

    public TabbySettings settings;
    @Nullable
    public ServerSettings serverSettings;

    private File dataFolder;
    private InetSocketAddress currentServer;

    public TabbyChat(File configPath) {
        super();
        this.dataFolder = new File(configPath, TabbyRef.MOD_ID);
    }

    public static TabbyChat getInstance() {
        return (TabbyChat) getAPI();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    @Override
    public VersionData getVersionData() {
        return new Version();
    }

    @Override
    public Chat getChat() {
        return GuiNewChatTC.getInstance().getChatManager();
    }

    @Override
    public AddonManager getAddonManager() {
        return this.addonManager;
    }
    
    public ForgeProxy getForgeProxy() {
        return forgeProxy;
    }
    
    @Override
    public void setForgeProxy(ForgeProxy forgeProxy) {
        this.forgeProxy = forgeProxy;
    }

    public TabbyEvents getEventManager() {
        return this.events;
    }

    public Spellcheck getSpellcheck() {
        return spellcheck;
    }

    @Override
    public ChatBox getGui() {
        return GuiNewChatTC.getInstance().getChatManager().getChatBox();
    }

    public void openSettings(SettingPanel<?> setting) {
        GuiSettingsScreen screen = new GuiSettingsScreen(setting);
        Minecraft.getMinecraft().displayGuiScreen(screen);
    }

    public InetSocketAddress getCurrentServer() {
        return this.currentServer;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public void init() {

        addonManager = new TabbyAddonManager();
        events = new TabbyEvents(addonManager);
        spellcheck = new Spellcheck(new File(getDataFolder(), "user.dic"));

        // Set global settings
        settings = new TabbySettings();
        // Load settings
        settings.loadSettingsFile();
        // Save settings
        settings.saveSettingsFile();

        addonManager.registerListener(new ChatAddonAntiSpam());
        addonManager.registerListener(new FilterAddon());
        addonManager.registerListener(new ChatLogging(new File("logs/chat")));

        addFilterVariables();
        MnmUtils.getInstance().setChatProxy(new TabbedChatProxy());
    }

    private void addFilterVariables() {
        final Minecraft mc = Minecraft.getMinecraft();
        addonManager.setFilterConstant("player", mc.getSession().getUsername());
        final Function<EntityPlayer, String> names = new Function<EntityPlayer, String>() {
            @Override
            public String apply(EntityPlayer player) {
                return Pattern.quote(player.getCommandSenderName());
            }
        };
        addonManager.setFilterVariable("onlineplayer", new FilterVariable() {
            @Override
            public String getVar() {
                @SuppressWarnings("unchecked")
                List<String> playerNames = Lists.transform(mc.theWorld.playerEntities, names);
                return Joiner.on('|').appendTo(new StringBuilder("(?:"), playerNames).append(')').toString();
            }
        });
    }

    public void changeChatScreen(GuiScreen currentScreen) {
        if (currentScreen instanceof GuiChat && !(currentScreen instanceof GuiChatTC)) {
            Minecraft mc = Minecraft.getMinecraft();
            if (currentScreen instanceof GuiSleepMP) {
                mc.displayGuiScreen(new GuiSleepTC());
            } else {
                // Get the default text via Reflection
                String inputBuffer = TabbyPrivateFields.defaultInputFieldText.get((GuiChat) currentScreen);
                mc.displayGuiScreen(new GuiChatTC(inputBuffer));
            }
        }
    }

    public void onJoin(SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            this.currentServer = (InetSocketAddress) address;
        } else {
            this.currentServer = null;
        }

        // Set server settings
        serverSettings = new ServerSettings(currentServer);
        serverSettings.loadSettingsFile();
        serverSettings.saveSettingsFile();

        try {
            hookIntoChat(Minecraft.getMinecraft().ingameGUI);
        } catch (Exception e) {
            LOGGER.fatal("Unable to hook into chat.  This is bad.", e);
        }
        // load chat
        File conf = serverSettings.getFile().getParentFile();
        try {
            GuiNewChatTC.getInstance().getChatManager().loadFrom(conf);
        } catch (Exception e) {
            LOGGER.warn("Unable to load chat data.", e);
        }
    }

    private static void hookIntoChat(GuiIngame guiIngame) throws Exception {
        if (!GuiNewChatTC.class.isAssignableFrom(guiIngame.getChatGUI().getClass())) {
            TabbyPrivateFields.persistantChatGUI.setFinal(guiIngame, GuiNewChatTC.getInstance());
            LOGGER.info("Successfully hooked into chat.");
        }
    }

    private class Version implements VersionData {
        @Override
        public double getRevision() {
            return TabbyRef.MOD_REVISION;
        }

        @Override
        public String getVersion() {
            return TabbyRef.MOD_VERSION;
        }
    }
}
