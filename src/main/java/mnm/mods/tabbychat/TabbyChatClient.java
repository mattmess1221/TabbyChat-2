package mnm.mods.tabbychat;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent;
import mnm.mods.tabbychat.core.GuiNewChatTC;
import mnm.mods.tabbychat.extra.ChatAddonAntiSpam;
import mnm.mods.tabbychat.extra.ChatLogging;
import mnm.mods.tabbychat.extra.filters.FilterAddon;
import mnm.mods.tabbychat.extra.spell.Spellcheck;
import mnm.mods.tabbychat.gui.settings.GuiSettingsScreen;
import mnm.mods.tabbychat.settings.ServerSettings;
import mnm.mods.tabbychat.settings.TabbySettings;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.ChatTextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConnecting;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.regex.Matcher;

public class TabbyChatClient {

    private static final TabbyChatClient instance = new TabbyChatClient();
    private ChatManager chatManager;
    private Spellcheck spellcheck;

    private TabbySettings settings;
    private ServerSettings serverSettings;

    public static TabbyChatClient getInstance() {
        return instance;
    }

    private TabbyChatClient() {
    }

    public ChatManager getChatManager() {
        return chatManager;
    }

    public Spellcheck getSpellcheck() {
        return spellcheck;
    }

    public TabbySettings getSettings() {
        return settings;
    }

    public ServerSettings getServerSettings() {
        Minecraft mc = Minecraft.getInstance();
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection == null) {
            serverSettings = null;
        } else {
            // this is probably InetSocketAddress
            SocketAddress address = connection.getNetworkManager().getRemoteAddress();
            if (serverSettings == null || !serverSettings.getSocket().equals(address)) {

                // Set server settings
                serverSettings = new ServerSettings(TabbyChat.dataFolder, address);
                onJoinServer();
            }
        }
        return serverSettings;
    }

    @SubscribeEvent
    public void init(FMLClientSetupEvent event) {
        // Set global settings
        settings = new TabbySettings(TabbyChat.dataFolder);
        try {
            settings.load();
        } catch (IOException e) {
            TabbyChat.logger.warn("Failed to load or save config.", e);
        }
    }

    @SubscribeEvent
    public void onLoadingFinished(FMLLoadCompleteEvent event) {

        TabbyChat.logger.info("Minecraft load complete!");

        Minecraft mc = Minecraft.getInstance();

        spellcheck = new Spellcheck(TabbyChat.dataFolder);        // Keeps the current language updated whenever it is changed.
        IReloadableResourceManager irrm = (IReloadableResourceManager) mc.getResourceManager();
        irrm.addReloadListener(spellcheck);

        chatManager = ChatManager.instance();

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, this::removeChannelTags);
        MinecraftForge.EVENT_BUS.register(new ChatAddonAntiSpam(chatManager));
        MinecraftForge.EVENT_BUS.register(new FilterAddon(chatManager));
        MinecraftForge.EVENT_BUS.register(new ChatLogging(FMLPaths.GAMEDIR.get().resolve("logs/chat")));
    }

    private void removeChannelTags(MessageAddedToChannelEvent.Pre event) {
        if (settings.advanced.hideTag.get() && event.getChannel() != ChatManager.DEFAULT_CHANNEL) {
            ChannelPatterns pattern = getServerSettings().general.channelPattern.get();

            ITextComponent text = event.getText();
            Matcher matcher = pattern.getPattern().matcher(event.getText().getString());
            if (matcher.find()) {
                event.setText(ChatTextUtils.subChat(text, matcher.end()));
            }
        }
    }

    private void onJoinServer() {

        try {
            serverSettings.load();
        } catch (IOException e) {
            TabbyChat.logger.warn("Unable to load or save server config", e);
        }

        // load chat
        try {
            Path conf = getServerSettings().getPath().getParent();
            chatManager.loadFrom(conf);
        } catch (Exception e) {
            TabbyChat.logger.warn("Unable to load chat data.", e);
        }

    }

    @Mod.EventBusSubscriber(modid = TabbyChat.MODID)
    private static class StartListener {
        @SubscribeEvent
        public static void onGuiOpen(GuiOpenEvent event) {
            GuiScreen gui = event.getGui();
            // check for both main menu and connecting because of launch args
            if (gui instanceof GuiMainMenu || gui instanceof GuiConnecting) {
                Minecraft mc = Minecraft.getInstance();
                hookIntoChat(mc.ingameGUI, new GuiNewChatTC(mc, instance));
                MinecraftForge.EVENT_BUS.unregister(StartListener.class);
            }
        }
    }

    private static void hookIntoChat(GuiIngame guiIngame, GuiNewChat chat) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(GuiIngame.class, guiIngame, chat, "field_73840_e");
//            guiIngame.persistantChatGUI = chat;
            TabbyChat.logger.info("Successfully hooked into chat.");
        } catch (Throwable e) {
            TabbyChat.logger.fatal("Unable to hook into chat. This is bad.", e);
        }
    }
}
