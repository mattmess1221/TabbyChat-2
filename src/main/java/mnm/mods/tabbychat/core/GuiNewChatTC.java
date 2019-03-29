package mnm.mods.tabbychat.core;

import com.google.common.collect.Sets;
import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.ChatManager;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.TabbyChatClient;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.api.ChannelStatus;
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent;
import mnm.mods.tabbychat.gui.ChatBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GuiNewChatTC extends GuiNewChat {

    private Minecraft mc;

    private TabbyChatClient tc = TabbyChatClient.getInstance();
    private ChatManager chat;

    private int prevScreenWidth;
    private int prevScreenHeight;

    public GuiNewChatTC(Minecraft minecraft, ChatManager manager) {
        super(minecraft);
        mc = minecraft;
        chat = manager;

        this.prevScreenHeight = mc.mainWindow.getHeight();
    }

    @Override
    public void refreshChat() {
        chat.getChatBox().tick();
    }

    @Override
    public void clearChatMessages(boolean sent) {
        checkThread(() -> {
            chat.clearMessages();
            if (sent) {
                this.getSentMessages().clear();
            }
        }).run();
    }

    @Override
    public void render(int i) {
        if (prevScreenHeight != mc.mainWindow.getHeight() || prevScreenWidth != mc.mainWindow.getWidth()) {

            chat.getChatBox().onScreenHeightResize(prevScreenWidth, prevScreenHeight, mc.mainWindow.getWidth(), mc.mainWindow.getHeight());

            prevScreenWidth = mc.mainWindow.getWidth();
            prevScreenHeight = mc.mainWindow.getHeight();
        }

        if (getChatOpen())
            return;

        ChatBox chatbox = chat.getChatBox();
        double scale = mc.gameSettings.chatScale;

        GlStateManager.popMatrix(); // ignore what GuiIngame did.
        GlStateManager.pushMatrix();

        // Scale it accordingly
        GlStateManager.scaled(scale, scale, 1.0D);

        int mouseX = (int) mc.mouseHelper.getMouseX();
        int mouseY = (int) (-mc.mouseHelper.getMouseY() - 1);
        chatbox.render(mouseX, mouseY, 0);

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix(); // push to avoid gl errors
    }

    @Override
    public void printChatMessageWithOptionalDeletion(ITextComponent ichat, int id) {
        checkThread(() -> this.addMessage(ichat, id)).run();
    }

    public void addMessage(ITextComponent ichat, int id) {
        // chat listeners
        ChatReceivedEvent chatevent = new ChatReceivedEvent(ichat, id);
        chatevent.channels.add(ChatChannel.DEFAULT_CHANNEL);
        MinecraftForge.EVENT_BUS.post(chatevent);
        // chat filters
        ichat = chatevent.text;
        id = chatevent.id;
        if (ichat != null && !ichat.getString().isEmpty()) {
            if (id != 0) {
                // send removable msg to current channel
                chatevent.channels.clear();
                chatevent.channels.add(this.chat.getActiveChannel());
            }
            if (chatevent.channels.contains(ChatChannel.DEFAULT_CHANNEL) && chatevent.channels.size() > 1
                    && !tc.getServerSettings().general.useDefaultTab.get()) {
                chatevent.channels.remove(ChatChannel.DEFAULT_CHANNEL);
            }
            boolean msg = !chatevent.channels.contains(this.chat.getActiveChannel());
            final Set<String> ignored = Sets.newHashSet(this.tc.getServerSettings().general.ignoredChannels.get());
            Set<Channel> channels = chatevent.channels.stream()
                    .filter(it -> !ignored.contains(it.getName()))
                    .collect(Collectors.toSet());
            for (Channel channel : channels) {
                channel.addMessage(ichat, id);
                if (msg) {
                    channel.setStatus(ChannelStatus.UNREAD);
                }
            }
            TabbyChat.logger.info("[CHAT] " + ichat.getString());
            this.chat.getChatBox().tick();
        }
    }

    @Override
    public void deleteChatLine(int id) {
        checkThread(() -> chat.removeMessages(id)).run();
    }

    private Runnable checkThread(Runnable runnable) {
        if (!mc.isCallingFromMinecraftThread()) {
            mc.addScheduledTask(runnable);
            TabbyChat.logger.warn("Tried to modify chat from thread {}. To prevent a crash, it has been scheduled on the main thread.", Thread.currentThread().getName(), new Exception());
            return () -> {};
        }
        return runnable;
    }

    @Override
    public void resetScroll() {
        chat.getChatBox().getChatArea().resetScroll();
        super.resetScroll();
    }

    @Nonnull
    @Override
    public List<String> getSentMessages() {
        return super.getSentMessages();
    }

    @Override
    @Nullable
    public ITextComponent func_194817_a /*getChatComponent*/ (double clickX, double clickY) {
        return chat.getChatBox().getChatArea().getChatComponent((int) clickX, (int) clickY);
    }

    @Override
    public int getChatHeight() {
        return chat.getChatBox().getChatArea().getLocation().getHeight();
    }

    @Override
    public int getChatWidth() {
        return chat.getChatBox().getChatArea().getLocation().getWidth();
    }

}
