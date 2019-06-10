package mnm.mods.tabbychat.client;

import mnm.mods.tabbychat.TCMarkers;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent;
import mnm.mods.tabbychat.client.core.GuiNewChatTC;
import mnm.mods.tabbychat.client.extra.ChatAddonAntiSpam;
import mnm.mods.tabbychat.client.extra.ChatLogging;
import mnm.mods.tabbychat.client.extra.filters.FilterAddon;
import mnm.mods.tabbychat.client.extra.spell.Spellcheck;
import mnm.mods.tabbychat.client.settings.ServerSettings;
import mnm.mods.tabbychat.client.settings.TabbySettings;
import mnm.mods.tabbychat.util.ChannelPatterns;
import mnm.mods.tabbychat.util.ChatTextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLPaths;

import java.net.SocketAddress;
import java.nio.file.Path;
import java.util.regex.Matcher;

public class TabbyChatClient {

    private static TabbyChatClient instance;
    private ChatManager chatManager;
    private Spellcheck spellcheck;

    private TabbySettings settings;
    private ServerSettings serverSettings;

    public static TabbyChatClient getInstance() {
        return instance;
    }

    public TabbyChatClient(Path dataFolder) {
        instance = this;
        // Set global settings
        settings = new TabbySettings(dataFolder);
        settings.load();
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
        ClientPlayNetHandler connection = mc.getConnection();
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
    public void onLoadingFinished(FMLLoadCompleteEvent event) {

        TabbyChat.logger.info(TCMarkers.STARTUP, "Minecraft load complete!");

        Minecraft mc = Minecraft.getInstance();

        spellcheck = new Spellcheck(TabbyChat.dataFolder);        // Keeps the current language updated whenever it is changed.
        IReloadableResourceManager irrm = (IReloadableResourceManager) mc.getResourceManager();
        irrm.func_219534_a/*addReloadListener*/(spellcheck);

        chatManager = ChatManager.instance();

        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, this::removeChannelTags);
        MinecraftForge.EVENT_BUS.register(new ChatAddonAntiSpam(chatManager));
        MinecraftForge.EVENT_BUS.register(new FilterAddon(chatManager));
        MinecraftForge.EVENT_BUS.register(new ChatLogging(FMLPaths.GAMEDIR.get().resolve("logs/chat")));
    }

    private void removeChannelTags(MessageAddedToChannelEvent.Pre event) {
        if (settings.advanced.hideTag.get() && event.getChannel() != DefaultChannel.INSTANCE) {
            ChannelPatterns pattern = getServerSettings().general.channelPattern.get();

            ITextComponent text = event.getText();
            Matcher matcher = pattern.getPattern().matcher(event.getText().getString());
            if (matcher.find()) {
                event.setText(ChatTextUtils.subChat(text, matcher.end()));
            }
        }
    }

    private void onJoinServer() {

        serverSettings.load();

        // load chat
        try {
            Path conf = getServerSettings().getPath().getParent();
            chatManager.loadFrom(conf);
        } catch (Exception e) {
            TabbyChat.logger.warn(TCMarkers.CHATBOX, "Unable to load chat data.", e);
        }

    }

    @Mod.EventBusSubscriber(modid = TabbyChat.MODID, value = Dist.CLIENT)
    private static class StartListener {
        @SubscribeEvent
        public static void onGuiOpen(GuiOpenEvent event) {
            Screen gui = event.getGui();
            // check for both main menu and connecting because of launch args
            if (gui instanceof MainMenuScreen || gui instanceof ConnectingScreen) {
                Minecraft mc = Minecraft.getInstance();
                hookIntoChat(mc.field_71456_v/*ingameGUI*/, new GuiNewChatTC(mc, instance));
                MinecraftForge.EVENT_BUS.unregister(StartListener.class);
            }
        }
    }

    @Mod.EventBusSubscriber(modid = TabbyChat.MODID, value = Dist.CLIENT)
    private static class NullScreenListener {
        // Listens for a null GuiScreen. Null means we're in-game
        // TODO workaround for lack of client network events. Replace when possible
        @SubscribeEvent
        public static void onGuiOpen(GuiOpenEvent event) {
            if (event.getGui() == null) {
                instance.getServerSettings();
            }
        }
    }

    private static void hookIntoChat(IngameGui guiIngame, NewChatGui chat) {
        try {
            ObfuscationReflectionHelper.setPrivateValue(IngameGui.class, guiIngame, chat, "field_73840_e");
//            guiIngame.persistantChatGUI = chat;
            TabbyChat.logger.info(TCMarkers.STARTUP, "Successfully hooked into chat.");
        } catch (Throwable e) {
            TabbyChat.logger.fatal(TCMarkers.STARTUP, "Unable to hook into chat. This is bad.", e);
        }
    }
}
