package mnm.mods.tabbychat;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.mumfrey.liteloader.core.LiteLoader;

import mnm.mods.tabbychat.api.AddonManager;
import mnm.mods.tabbychat.api.Chat;
import mnm.mods.tabbychat.api.TabbyAPI;
import mnm.mods.tabbychat.api.VersionData;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.core.api.TabbyAddonManager;
import mnm.mods.tabbychat.core.overlays.IGuiIngame;
import mnm.mods.tabbychat.extra.ChatAddonAntiSpam;
import mnm.mods.tabbychat.extra.ChatLogging;
import mnm.mods.tabbychat.extra.filters.FilterAddon;
import mnm.mods.tabbychat.extra.spell.Spellcheck;
import mnm.mods.tabbychat.gui.settings.GuiSettingsScreen;
import mnm.mods.tabbychat.settings.ServerSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.MnmUtils;
import mnm.mods.util.gui.config.SettingPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.entity.player.EntityPlayer;

public class TabbyChat extends TabbyAPI {

    private static final Logger LOGGER = LogManager.getLogger(TabbyRef.MOD_ID);

    private ChatManager chatManager;
    private GuiNewChatTC chatGui;
    private AddonManager addonManager;
    private EventBus bus = new EventBus();
    private Spellcheck spellcheck;

    public TabbySettings settings;
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
        return chatManager;
    }

    public GuiNewChatTC getChatGui() {
        return chatGui;
    }

    @Override
    public AddonManager getAddonManager() {
        return this.addonManager;
    }

    @Override
    public EventBus getBus() {
        return bus;
    }

    public Spellcheck getSpellcheck() {
        return spellcheck;
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

        // Set global settings
        settings = new TabbySettings();
        LiteLoader.getInstance().registerExposable(settings, null);

        spellcheck = new Spellcheck(getDataFolder());

        addonManager = new TabbyAddonManager();

        // Keeps the current language updated whenever it is changed.
        IReloadableResourceManager irrm = (IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager();
        irrm.registerReloadListener(spellcheck);

        bus.register(new ChatAddonAntiSpam());
        bus.register(new FilterAddon());
        bus.register(new ChatLogging(new File("logs/chat")));

        addFilterVariables();
        MnmUtils.INSTANCE.setChatProxy(new TabbedChatProxy());

    }

    public void postInit() {
        // gui related stuff should be done here
        chatManager = new ChatManager(this);
        chatGui = new GuiNewChatTC(Minecraft.getMinecraft(), chatManager);
    }

    private void addFilterVariables() {
        final Minecraft mc = Minecraft.getMinecraft();
        addonManager.setFilterConstant("player", mc.getSession().getUsername());
        final Function<EntityPlayer, String> names = player -> Pattern.quote(player.getName());
        addonManager.setFilterVariable("onlineplayer", () -> {
            List<String> playerNames = Lists.transform(mc.theWorld.playerEntities, names);
            return Joiner.on('|').appendTo(new StringBuilder("(?:"), playerNames).append(')').toString();
        });
    }

    public void onJoin(@Nullable SocketAddress address) {
        if (address instanceof InetSocketAddress) {
            this.currentServer = (InetSocketAddress) address;
        } else {
            this.currentServer = null;
        }

        // Set server settings
        serverSettings = new ServerSettings(currentServer);
        LiteLoader.getInstance().registerExposable(serverSettings, null);

        try {
            hookIntoChat(Minecraft.getMinecraft().ingameGUI);
        } catch (Exception e) {
            LOGGER.fatal("Unable to hook into chat.  This is bad.", e);
        }
        // load chat
        File conf = serverSettings.getFile().getParentFile();
        try {
            chatManager.loadFrom(conf);
        } catch (Exception e) {
            LOGGER.warn("Unable to load chat data.", e);
        }
    }

    private void hookIntoChat(GuiIngame guiIngame) throws Exception {
        if (!GuiNewChatTC.class.isAssignableFrom(guiIngame.getChatGUI().getClass())) {
//            TabbyPrivateFields.persistantChatGUI.setFinal(guiIngame, chatGui);
            ((IGuiIngame)guiIngame).setChatGUI(chatGui);
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
