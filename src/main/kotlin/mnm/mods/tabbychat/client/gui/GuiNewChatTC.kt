package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.CHATBOX
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.ChannelStatus
import mnm.mods.tabbychat.api.events.ChatMessageEvent.ChatReceivedEvent
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.DefaultChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.NewChatGui
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.util.text.ITextComponent
import net.minecraftforge.common.MinecraftForge

object GuiNewChatTC : NewChatGui(mc) {

    private var prevScreenWidth: Int = 0
    private var prevScreenHeight: Int = 0

    init {
        prevScreenHeight = mc.func_228018_at_().height

        MinecraftForge.EVENT_BUS.register(ComponentWrapper(ChatScreen::class, ChatBox))
    }

    override fun refreshChat() {
        ChatBox.tick()
    }

    override fun clearChatMessages(sent: Boolean) {
        checkThread {
            ChatManager.clearMessages()
            if (sent) {
                this.sentMessages.clear()
            }
        }
    }

    override fun render(i: Int) {
        if (prevScreenHeight != mc.func_228018_at_().height || prevScreenWidth != mc.func_228018_at_().width) {

            ChatBox.onScreenHeightResize(prevScreenWidth, prevScreenHeight, mc.func_228018_at_().width, mc.func_228018_at_().height)

            prevScreenWidth = mc.func_228018_at_().width
            prevScreenHeight = mc.func_228018_at_().height
        }

        if (chatOpen)
            return

        val scale = mc.gameSettings.chatScale

        RenderSystem.popMatrix() // ignore what GuiIngame did.
        RenderSystem.pushMatrix()

        // Scale it accordingly
        RenderSystem.scaled(scale, scale, 1.0)

        val mouseX = mc.mouseHelper.mouseX.toInt()
        val mouseY = (-mc.mouseHelper.mouseY - 1).toInt()
        ChatBox.render(mouseX, mouseY, 0f)

        RenderSystem.popMatrix()
        RenderSystem.pushMatrix() // push to avoid gl errors
    }

    override fun printChatMessageWithOptionalDeletion(ichat: ITextComponent, id: Int) {
        checkThread { addMessage(ichat, id) }
    }

    fun addMessage(ichat: ITextComponent, id: Int) {
        // chat listeners
        val chatevent = ChatReceivedEvent(ichat, id)
        chatevent.channels.add(DefaultChannel)
        MinecraftForge.EVENT_BUS.post(chatevent)
        // chat filters
        val ichat = chatevent.text
        val id = chatevent.id
        if (ichat != null && ichat.string.isNotEmpty()) {
            if (id != 0) {
                // send removable msg to current channel
                chatevent.channels.clear()
                chatevent.channels.add(ChatBox.activeChannel)
            }
            if (chatevent.channels.contains(DefaultChannel) && chatevent.channels.size > 1
                    && !TabbyChatClient.serverSettings.general.useDefaultTab.value) {
                chatevent.channels.remove(DefaultChannel)
            }
            val msg = !chatevent.channels.contains(ChatBox.activeChannel)
            val ignored = TabbyChatClient.serverSettings.general.ignoredChannels.value.toSet()
            val channels = chatevent.channels.asSequence()
                    .filter { it.name !in ignored }
                    .toSet()
            for (channel in channels) {
                ChatManager.addMessage(channel, ichat, id)
                if (msg) {
                    ChatBox.status[channel] = ChannelStatus.UNREAD
                }
            }
            TabbyChat.logger.info(CHATBOX, "[CHAT] " + ichat.string)
            ChatBox.tick()
        }
    }

    private fun checkThread(runnable: () -> Unit) {
        if (!mc.isOnExecutionThread) {
            mc.enqueue(runnable)
            TabbyChat.logger.warn(CHATBOX, "Tried to modify chat from thread {}. To prevent a crash, it has been scheduled on the main thread.", Thread.currentThread().name, Exception())
        } else {
            runnable()
        }
    }

    override fun resetScroll() {
        ChatBox.chatArea.resetScroll()
        super.resetScroll()
    }

    override fun getTextComponent(clickX: Double, clickY: Double): ITextComponent? {
        return ChatBox.chatArea.getTextComponent(clickX.toInt(), clickY.toInt())
    }

    override fun getChatHeight(): Int {
        return ChatBox.chatArea.location.height
    }

    override fun getChatWidth(): Int {
        return ChatBox.chatArea.location.width
    }
}
