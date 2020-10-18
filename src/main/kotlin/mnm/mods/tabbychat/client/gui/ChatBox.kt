package mnm.mods.tabbychat.client.gui

import com.google.common.collect.ImmutableSet
import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.ChannelStatus
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.ChannelImpl
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.DefaultChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.widget.MultiLineTextFieldWidget
import mnm.mods.tabbychat.client.util.ScaledDimension
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.gui.FocusableGui
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.IRenderable
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.renderer.Rectangle2d
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper.ceil
import net.minecraft.util.math.MathHelper.floor
import org.lwjgl.glfw.GLFW
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

object ChatBox : FocusableGui(), IRenderable, ILocatable {

    override var xPos: Int = 0
    override var yPos: Int = 0
    override var width: Int = 0
    override var height: Int = 0

    val GUI_LOCATION = ResourceLocation(MODID, "textures/chatbox.png")
    var chatArea: ChatArea
        private set
    var tray: ChatTray
        private set
    var chatInput: MultiLineTextFieldWidget
        private set
    var scrollbar: Scrollbar

    private val channels = ArrayList<Channel>()
    private val children = ArrayList<IGuiEventListener>()

    class ChannelStatusMap {
        private val channelStatus = mutableMapOf<Channel, ChannelStatus>()
        internal operator fun get(chan: Channel): ChannelStatus? {
            return channelStatus[chan]
        }

        operator fun set(chan: Channel, status: ChannelStatus?) {
            channelStatus.compute(chan) { _, old ->
                if (status?.ordinal ?: -1 < old?.ordinal ?: ChannelStatus.values().size + 1) {
                    status
                } else {
                    old
                }
            }
            if (status == ChannelStatus.ACTIVE) {
                chatArea.channel = chan
            }
        }
    }

    var status = ChannelStatusMap()

    var activeChannel: Channel = DefaultChannel
        set(channel) {
            val text = this.chatInput

            val prefix = text.text.trim { it <= ' ' }
            val oldChannel = field
            if (oldChannel is ChannelImpl) {
                if (oldChannel.isPrefixHidden && prefix.isEmpty() || prefix == oldChannel.prefix) {
                    // text is the prefix, so remove it.
                    text.text = ""
                    if (channel is ChannelImpl) {
                        if (!channel.isPrefixHidden && channel.prefix.isNotEmpty()) {
                            // target has prefix visible
                            text.text = channel.prefix + " "
                        }
                    }
                }
            }

            if (channel is ChannelImpl) {
                // set max text length
                val hidden = channel.isPrefixHidden
                val prefLength = if (hidden) channel.prefix.length + 1 else 0
                text.maxStringLength = ChatManager.MAX_CHAT_LENGTH - prefLength
            } else {
                text.maxStringLength = ChatManager.MAX_CHAT_LENGTH
            }

            // reset scroll
            // TODO per-channel scroll settings?
            if (channel !== field) {
                chatArea.resetScroll()
            }
            status[field] = null
            field = channel
            status[field] = ChannelStatus.ACTIVE
        }

    private lateinit var chat: ChatScreen

    private var locationDirty = false

    private fun saveLocation() {
        val loc = TabbyChatClient.settings.advanced.chatLocation
        loc.xPos = xPos
        loc.yPos = yPos
        loc.width = width
        loc.height = height

        TabbyChatClient.settings.save()
        locationDirty = true
    }

    init {
        val location = TabbyChatClient.settings.advanced.chatLocation
        xPos = location.xPos
        yPos = location.yPos
        width = location.width
        height = location.height

        tray = ChatTray(this)
        chatArea = ChatArea(this)
        chatInput = MultiLineTextFieldWidget(this, mc.fontRenderer, "")
        scrollbar = Scrollbar(chatArea)

        this.children += listOf<IGuiEventListener>(tray, chatArea, chatInput)

        this.channels.add(DefaultChannel)
        this.tray.addChannel(DefaultChannel)

        status[DefaultChannel] = ChannelStatus.ACTIVE

        FORGE_BUS.addListener(::messageScroller)
        FORGE_BUS.addListener(::addChatMessage)
    }

    override fun children(): MutableList<out IGuiEventListener> {
        return children
    }

    fun init(screen: Screen) {
        this.chat = screen as ChatScreen
        this.chat.commandSuggestionHelper.field_228095_d_ = chatInput
        val chan = activeChannel
        if (screen.defaultInputFieldText.isEmpty()
                && chan is ChannelImpl
                && !chan.isPrefixHidden
                && chan.prefix.isNotEmpty()) {
            screen.defaultInputFieldText = chan.prefix + " "
        }
        screen.inputField = chatInput
        chatInput.text = screen.defaultInputFieldText

        chatInput.setTextFormatter(screen.commandSuggestionHelper::func_228122_a_)
        chatInput.setResponder(screen::func_212997_a)

        val children = screen.children() as MutableList<IGuiEventListener>
        children[0] = chatInput
    }

    fun tick() {
        // try to fix the chatbox location to keep it in the window.
        if (normalizeLocation()) {
            ChatManager.markDirty(activeChannel)
            this.locationDirty = true
        }

        tray.tick()
        chatInput.tick()

        // finally, save the location if it changed.
        if (!draggingChatBox && locationDirty) {
            saveLocation()
            locationDirty = false
        }
    }

    private fun update() {
        chat.commandSuggestionHelper.suggestions?.apply {
            if (field_228138_b_ !is TCRect) {
                field_228138_b_ = TCRect(field_228138_b_)
            }
        }
    }

    private fun messageScroller(event: MessageAddedToChannelEvent.Post) {

        // compensate scrolling
        val chatbox = chatArea
        if (activeChannel === event.channel && chatbox.scrollPos > 0 && event.id == 0) {
            chatbox.scroll(1)
        }
    }

    private fun addChatMessage(event: MessageAddedToChannelEvent.Post) {
        val channel = event.channel
        addChannel(channel)
        status[channel] = ChannelStatus.UNREAD
    }

    fun addChannels(active: Collection<Channel>) {
        active.forEach { this.addChannel(it) }
    }

    fun getChannels(): Set<Channel> {
        return ImmutableSet.copyOf(this.channels)
    }

    private fun addChannel(channel: Channel) {
        if (!this.channels.contains(channel)) {
            this.channels.add(channel)
            tray.addChannel(channel)
            ChatManager.save()
        }

    }

    fun removeChannel(channel: Channel) {
        if (channels.contains(channel) && channel !== DefaultChannel) {
            channels.remove(channel)
            tray.removeChannel(channel)
        }
        if (activeChannel === channel) {
            activeChannel = DefaultChannel
        }
        ChatManager.save()
    }

    fun clearMessages() {
        this.channels.removeIf { it !== DefaultChannel }

        this.tray.clearMessages()
        status[DefaultChannel] = ChannelStatus.ACTIVE
    }

    /*
    private val settings get() = TabbyChatClient.serverSettings

    private fun runActivationCommand(channel: ChannelImpl) {
        var cmd = channel.command
        if (cmd.isEmpty()) {
            val pat = if (channel is UserChannel) {
                settings.general.messageCommand
            } else {
                settings.general.channelCommand
            }
            if (pat.isEmpty()) {
                return
            }
            var name = channel.name
            if (channel === DefaultChannel) {
                name = settings.general.defaultChannel
            }
            // insert the channel name
            cmd = pat.replace("{}", name)

        }
        if (cmd.startsWith("/")) {
            if (cmd.length > ChatManager.MAX_CHAT_LENGTH) {
                cmd = cmd.substring(0, ChatManager.MAX_CHAT_LENGTH)
            }
            mc.player?.sendChatMessage(cmd)
        }
    }

     */

    override fun render(x: Int, y: Int, parTicks: Float) {
        handleDragging(x.toDouble(), y.toDouble())

        this.chatArea.render(x, y, parTicks)

        if (mc.ingameGUI.chatGUI.chatOpen) {
            this.tray.render(x, y, parTicks)
            this.chatInput.render(x, y, parTicks)
            update()
            chat.commandSuggestionHelper.render(x, y)

            val itextcomponent = mc.ingameGUI.chatGUI.getTextComponent(x.toDouble(), y.toDouble())
            if (itextcomponent != null && itextcomponent.style.hoverEvent != null) {
                chat.renderComponentHoverEffect(itextcomponent, x, y)
            }
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            mc.ingameGUI.chatGUI.resetScroll()
            chat.sendMessage(chatInput.text)
            chatInput.text = chat.defaultInputFieldText

            if (!TabbyChatClient.settings.advanced.keepChatOpen) {
                mc.displayGuiScreen(null)
            }
            return true
        }

        if (chat.commandSuggestionHelper.onKeyPressed(keyCode, scanCode, modifiers)) {
            return true
        }
        if (keyCode == GLFW.GLFW_KEY_TAB) {
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    private var draggingChatBox = false
    private var resizing = false

    private var dragStartX = 0
    private var dragStartY = 0

    private var tempX = 0
    private var tempY = 0
    private var tempW = 0
    private var tempH = 0

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        if (chat.commandSuggestionHelper.onClick(x, y, button)) {
            return true
        }
        if (button == 0 && tray.contains(x.toInt(), y.toInt())
                || Screen.hasAltDown() && this.contains(x.toInt(), y.toInt())) {
            draggingChatBox = true
            resizing = tray.isHandleHovered(x, y)
            dragStartX = x.toInt()
            dragStartY = y.toInt()
            tempX = this.xPos
            tempY = this.yPos
            tempW = this.width
            tempH = this.height
        }

        if (mc.ingameGUI.chatGUI.chatOpen) {
            val itextcomponent = mc.ingameGUI.chatGUI.getTextComponent(x, y)
            if (itextcomponent != null && itextcomponent.style.clickEvent != null) {
                return chat.handleComponentClicked(itextcomponent)
            }
        }

        return super.mouseClicked(x, y, button)
    }

    private fun handleDragging(mx: Double, my: Double) {
        if (draggingChatBox) {
            if (resizing) {
//                xPos = tempX + mx.toInt() - dragStartX
                yPos = tempY + my.toInt() - dragStartY
                width = tempW + mx.toInt() - dragStartX
                height = tempH - my.toInt() + dragStartY
            } else {
                xPos = tempX + mx.toInt() - dragStartX
                yPos = tempY - dragStartY + my.toInt()
            }
            if (normalizeLocation()) {
                ChatManager.markDirty(activeChannel)
            }
        }
    }

    override fun mouseReleased(x: Double, y: Double, button: Int): Boolean {
        if (draggingChatBox) {
            draggingChatBox = false
            locationDirty = true
        }
        return super.mouseReleased(x, y, button)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        return this.chat.commandSuggestionHelper.onScroll(scroll) || super.mouseScrolled(x, y, scroll)
    }

    /**
     * Normalizes the chatbox to keep it in view.
     */
    private fun normalizeLocation(): Boolean {
        val scale = mc.gameSettings.chatScale

        // original dims
        val x = floor(xPos * scale)
        val y = floor(yPos * scale)
        val w = floor(width * scale)
        val h = floor(height * scale)

        // the new dims
        var w1 = w
        var h1 = h
        var x1 = x
        var y1 = y

        val screenW = mc.mainWindow.scaledWidth
        val screenH = mc.mainWindow.scaledHeight

        val hotbar = 25

        // limits for sizes
        val minW = 150
        val minH = 50
        val maxH = screenH - hotbar


        // calculate width and height first
        // used to calculate max x and y
        w1 = w1.coerceIn(minW, screenW)

        // this is different because height anchor is at the top
        // so is affected at the bottom.
        if (h1 < minH) {
            y1 -= minH - h1
            h1 = minH
        }
        if (h1 > maxH) {
            y1 += h1 - maxH
            h1 = maxH
        }

        // limits for position
        val minX = 0
        val minY = 0
        val maxX = screenW - w1
        val maxY = screenH - h1 - hotbar

        // calculate x and y coordinates
        x1 = x1.coerceIn(minX, maxX)
        y1 = y1.coerceIn(minY, maxY)

        // reset the location if it changed.
        if (x1 != x || y1 != y || w1 != w || h1 != h) {
            this.xPos = ceil(x1 / scale)
            this.yPos = ceil(y1 / scale)
            this.width = ceil(w1 / scale)
            this.height = ceil(h1 / scale)
            return true
        }
        return false
    }

    override fun getFocused(): IGuiEventListener? {
        return chatInput
    }

    fun onScreenHeightResize(oldWidth: Int, oldHeight: Int, newWidth: Int, newHeight: Int) {

        if (oldWidth == 0 || oldHeight == 0)
            return  // first time!

        // measure the distance from the bottom, then subtract from new height

        val oldDim = ScaledDimension(oldWidth, oldHeight)
        val newDim = ScaledDimension(newWidth, newHeight)

        val bottom = oldDim.scaledHeight - yPos
        val y = newDim.scaledHeight - bottom
        if (this.yPos != y) {
            this.yPos = y
            locationDirty = true
        }
    }

    private class TCRect(private val parent: Rectangle2d) : Rectangle2d(0, 0, 0, 0) {

        override fun getX(): Int {
            return parent.x
        }

        override fun getY(): Int {
            chatInput.y
            return chatInput.y - parent.height + chatInput.wrappedLines.size * mc.fontRenderer.FONT_HEIGHT
        }

        override fun getWidth(): Int {
            return parent.width
        }

        override fun getHeight(): Int {
            return parent.height
        }

        override fun contains(x: Int, y: Int): Boolean {
            return this.x <= x && x <= this.x + this.width && this.y <= y && y <= this.y + this.height
        }
    }

}
