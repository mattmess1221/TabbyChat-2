package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.ChannelStatus
import mnm.mods.tabbychat.client.DefaultChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.*
import net.minecraft.client.gui.AbstractGui
import net.minecraft.client.gui.FocusableGui
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.IRenderable
import net.minecraft.client.gui.screen.Screen
import org.lwjgl.glfw.GLFW
import java.util.*

class ChatTray(private val chat: ChatBox) : FocusableGui(),
        IRenderable, IGuiEventListener, ILocatable {

    override val xPos get() = chat.xPos
    override val yPos get() = chat.yPos
    override val width get() = chat.width
    override val height get() = mc.fontRenderer.FONT_HEIGHT + 4

    private val toggle = ToggleButton()
    private val handle = ChatHandle(chat)

    private val channels = ArrayList<ChatTab>(20)

    private val children = ArrayList<IGuiEventListener>()

    init {

        children += listOf<IGuiEventListener>(toggle, handle)
//        tabList = this.add(GuiPanel(), BorderLayout.Position.CENTER) {
//            layout = FlowLayout()
//        }
//        this.add(ChatPanel(), BorderLayout.Position.EAST) {
//            layout = FlowLayout()
//            add(ToggleButton())
//            add(handle)
//        }
    }

    override fun children() = children

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (mc.ingameGUI.chatGUI.chatOpen) {
            handle.render(x, y, parTicks)
            toggle.render(x, y, parTicks)

            RenderSystem.enableBlend()
            RenderSystem.color4f(1f, 1f, 1f, mc.gameSettings.chatOpacity.toFloat())
            drawModalCorners(xPos, yPos, width, height, MODAL)
            RenderSystem.disableBlend()
        }

        for (c in channels) {

            val status = ChatBox.status[c.channel]
            if (mc.ingameGUI.chatGUI.chatOpen
                    || status != null && status > ChannelStatus.PINGED && TabbyChatClient.settings.general.unreadFlashing
                    || TabbyChatClient.settings.advanced.visibility === LocalVisibility.ALWAYS) {
                c.render(x, y, parTicks)
            }
        }
//        super.render(x, y, parTicks)
    }

    fun tick() {
//        super.tick()
//        val panel = parent
//        if (panel != null) {
//            secondaryColor = panel.secondaryColorProperty.with(a = { it / 4 * 3 })
//        }
    }

    fun addChannel(channel: Channel) {
        val tab = ChatTab(channel, channels.size)
        channels += tab
        children += tab
    }

    fun removeChannel(channel: Channel) {
        channels.removeAll { it.channel == channel }
    }

    fun clearMessages() {
        children.clear()
        children += listOf<IGuiEventListener>(this.handle, this.toggle)

        this.channels.clear()
        addChannel(DefaultChannel)
        ChatBox.status[DefaultChannel] = ChannelStatus.ACTIVE
    }

    internal fun isHandleHovered(x: Double, y: Double): Boolean {
        return handle.contains(x.toInt(), y.toInt())
    }

    private inner class ToggleButton : IRenderable, IGuiEventListener, ILocatable {

        override val xPos get() = this@ChatTray.xPos + this@ChatTray.width - this.width - this@ChatTray.handle.width - 3
        override val yPos get() = this@ChatTray.yPos + 3
        override val width get() = this@ChatTray.height - 4
        override val height get() = this@ChatTray.height - 4

        private var value by property(TabbyChatClient.settings.advanced::keepChatOpen)

        override fun render(x: Int, y: Int, parTicks: Float) {
            RenderSystem.enableBlend()
            val opac = (mc.gameSettings.chatOpacity * 255).toInt() shl 24
            renderBorders(x1 + 2, y1 + 2, x2 - 2, y2 - 2, 0x999999 or opac)
            if (value) {
                fill(x1 + 3, y1 + 3, x2 - 3, y2 - 3, 0xaaaaaa or opac)
            }
        }

        override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
            if (this.contains(x.toInt(), y.toInt()) && button == 0) {
                value = !value
                return true
            }
            return false
        }
    }

    private fun getTabXPos(index: Int): Int {
        return this.channels.stream().limit(index.toLong())
                .mapToInt { it.width }
                .sum() + this.xPos
    }

    private fun updateIndexes() {
        for (c in channels.indices) {
            channels[c].index = c
        }
    }

    private inner class ChatTab(val channel: Channel, var index: Int) : AbstractGui(), IGuiEventListener, IRenderable, ILocatable {

        private val genericChannelName get() = "<${channel.displayName}>"
        override val xPos: Int get() = this@ChatTray.getTabXPos(index)
        override val yPos: Int get() = this@ChatTray.yPos
        override val width get() = mc.fontRenderer.getStringWidth(genericChannelName) + 8
        override val height get() = this@ChatTray.height

        val text: String
            get() = when (ChatBox.status[channel]) {
                ChannelStatus.ACTIVE -> "[${channel.displayName}]"
                ChannelStatus.UNREAD -> "<${channel.displayName}>"
                else -> channel.displayName
            }

        override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
            if (this.contains(x, y) && isValidClickButton(button)) {
                when (button) {
                    // activate or remove channel
                    GLFW.GLFW_MOUSE_BUTTON_LEFT -> handleLeftClick()
                    // Open channel options
                    GLFW.GLFW_MOUSE_BUTTON_RIGHT -> handleRightClick()
                    // remove the channel
                    GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> handleMiddleClick()
                }
                return true
            }
            return false
        }

        private fun isValidClickButton(button: Int): Boolean {
            return button in intArrayOf(
                    GLFW.GLFW_MOUSE_BUTTON_LEFT,
                    GLFW.GLFW_MOUSE_BUTTON_RIGHT,
                    GLFW.GLFW_MOUSE_BUTTON_MIDDLE
            )
        }

        private fun handleLeftClick() {
            if (Screen.hasShiftDown()) {
                // Remove channel
                ChatBox.removeChannel(this.channel)
            } else {
                // Enable channel, disable others
                ChatBox.activeChannel = this.channel
            }
        }

        private fun handleRightClick() {
            // TODO reimplement settings
//        mc.displayGuiScreen(GuiSettingsScreen(channel))
        }

        private fun handleMiddleClick() {
            ChatBox.removeChannel(this.channel)
        }

        override fun render(x: Int, y: Int, parTicks: Float) {
            RenderSystem.enableBlend()
            RenderSystem.color4f(1f, 1f, 1f, mc.gameSettings.chatOpacity.toFloat())
            drawModalCorners(x1, y1, width, height, getStatusModal(this.contains(x, y)))

            val txtX = xCenter
            val txtY = yCenter - 2

            val color = Colors.WHITE.with(a = (mc.gameSettings.chatOpacity * 255).toInt())
            this.drawCenteredString(mc.fontRenderer, this.text, txtX, txtY, color)
            RenderSystem.disableBlend()
        }

        private fun getStatusModal(hovered: Boolean): TexturedModal {
            if (hovered) {
                return TAB_HOVERED
            }
            return when (ChatBox.status[channel]) {
                ChannelStatus.ACTIVE -> TAB_ACTIVE
                ChannelStatus.UNREAD -> TAB_UNREAD
                ChannelStatus.PINGED -> TAB_PINGED
                else -> TAB_PLAIN
            }
        }

    }

    companion object {
        private val MODAL = TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 202)
        private val TAB_ACTIVE = TexturedModal(ChatBox.GUI_LOCATION, 0, 0, 50, 14)
        private val TAB_UNREAD = TexturedModal(ChatBox.GUI_LOCATION, 50, 0, 50, 14)
        private val TAB_PINGED = TexturedModal(ChatBox.GUI_LOCATION, 100, 0, 50, 14)
        private val TAB_HOVERED = TexturedModal(ChatBox.GUI_LOCATION, 150, 0, 50, 14)
        private val TAB_PLAIN = TexturedModal(ChatBox.GUI_LOCATION, 200, 0, 50, 14)
    }
}
