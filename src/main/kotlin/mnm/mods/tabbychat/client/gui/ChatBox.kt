package mnm.mods.tabbychat.client.gui

import com.google.common.collect.ImmutableSet
import mnm.mods.tabbychat.MODID
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.ChannelStatus
import mnm.mods.tabbychat.api.events.MessageAddedToChannelEvent
import mnm.mods.tabbychat.client.*
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.client.util.ScaledDimension
import mnm.mods.tabbychat.util.*
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.renderer.Rectangle2d
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.glfw.GLFW
import kotlin.math.max
import kotlin.math.min

object ChatBox : GuiPanel() {

    val GUI_LOCATION = ResourceLocation(MODID, "textures/chatbox.png")
    var chatArea: ChatArea
        private set
    var tray: ChatTray
        private set
    var chatInput: TextBox
        private set

    private var dragMode: Boolean = false
    private var drag: Vec2i? = null
    private var tempbox: Location? = null

    private val channels = ArrayList<AbstractChannel>()

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
            if (status == ChannelStatus.ACTIVE && chan is AbstractChannel) {
                chatArea.channel = chan
            }
        }
    }

    var status = ChannelStatusMap()

    var activeChannel: AbstractChannel = DefaultChannel
        set(channel) {
            val text = this.chatInput

            val prefix = text.text.trim { it <= ' ' }
            if (field.isPrefixHidden && prefix.isEmpty() || prefix == field.prefix) {
                // text is the prefix, so remove it.
                text.text = ""
                if (!channel.isPrefixHidden && channel.prefix.isNotEmpty()) {
                    // target has prefix visible
                    text.textField.delegate.text = channel.prefix + " "
                }
            }
            // set max text length
            val hidden = channel.isPrefixHidden
            val prefLength = if (hidden) channel.prefix.length + 1 else 0

            text.textField.delegate.maxStringLength = ChatManager.MAX_CHAT_LENGTH - prefLength

            // reset scroll
            // TODO per-channel scroll settings?
            if (channel !== field) {
                chatArea.resetScroll()
            }
            status[field] = null
            field = channel
            status[field] = ChannelStatus.ACTIVE

            runActivationCommand(channel)
        }

    private lateinit var chat: ChatScreen

    // save bounds
    override var location: ILocation = TabbyChatClient.settings.advanced.chatLocation
        set(location) {
            if (field != location) {
                field = location
                val sett = TabbyChatClient.settings
                sett.advanced.chatLocation.merge(location)
                sett.save()
            }
        }

    init {
        layout = BorderLayout()

        tray = this.add(ChatTray(), BorderLayout.Position.NORTH)
        chatArea = this.add(ChatArea(), BorderLayout.Position.CENTER)
        chatInput = this.add(TextBox, BorderLayout.Position.SOUTH)
        this.add(Scrollbar(chatArea), BorderLayout.Position.EAST)

        this.channels.add(DefaultChannel)
        this.tray.addChannel(DefaultChannel)

        status[DefaultChannel] = ChannelStatus.ACTIVE

        super.tick()

        MinecraftForge.EVENT_BUS.listen<MessageAddedToChannelEvent.Post> {
            messageScroller(it)
        }
        MinecraftForge.EVENT_BUS.listen<MessageAddedToChannelEvent.Post> {
            addChatMessage(it)
        }
    }

    override fun init(screen: Screen) {
        this.chat = screen as ChatScreen
        this.chat.field_228174_e_.field_228095_d_ = chatInput.textField.delegate
        val chan = activeChannel
        if (screen.defaultInputFieldText.isEmpty()
                && !chan.isPrefixHidden
                && chan.prefix.isNotEmpty()) {
            screen.defaultInputFieldText = chan.prefix + " "
        }
        val text = chatInput.textField
        screen.inputField = text.delegate
        text.value = screen.defaultInputFieldText

        chatInput.textFormatter = screen.field_228174_e_::func_228122_a_
        text.delegate.setResponder { screen.func_212997_a(it) }

        val children = screen.children() as MutableList<IGuiEventListener>
        children[0] = ChatBox

        super.init(screen)
    }

    override fun tick() {
        location = normalizeLocation(location)
        super.tick()
    }

    private fun update() {
        chat.field_228174_e_.field_228108_q_?.apply {
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
        val channel = event.channel as AbstractChannel
        addChannel(channel)
        status[channel] = ChannelStatus.UNREAD
    }

    fun addChannels(active: Collection<AbstractChannel>) {
        active.forEach { this.addChannel(it) }
    }

    fun getChannels(): Set<AbstractChannel> {
        return ImmutableSet.copyOf(this.channels)
    }

    private fun addChannel(channel: AbstractChannel) {
        if (!this.channels.contains(channel)) {
            this.channels.add(channel)
            tray.addChannel(channel)
            ChatManager.save()
        }

    }

    fun removeChannel(channel: AbstractChannel) {
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

    private val settings get() = TabbyChatClient.serverSettings

    private fun runActivationCommand(channel: AbstractChannel) {
        var cmd = channel.command
        if (cmd.isEmpty()) {
            val pat = if (channel is UserChannel) {
                settings.general.messageCommand.value
            } else {
                settings.general.channelCommand.value
            }
            if (pat.isEmpty()) {
                return
            }
            var name = channel.name
            if (channel === DefaultChannel) {
                name = settings.general.defaultChannel.value
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

    override fun render(x: Int, y: Int, parTicks: Float) {
        update()
        handleDragging(x.toDouble(), y.toDouble())

        super.render(x, y, parTicks)
        if (mc.ingameGUI.chatGUI.chatOpen) {
            chat.field_228174_e_.func_228114_a_(x, y)

            val itextcomponent = mc.ingameGUI.chatGUI.getTextComponent(x.toDouble(), y.toDouble())
            if (itextcomponent != null && itextcomponent.style.hoverEvent != null) {
                chat.renderComponentHoverEffect(itextcomponent, x, y)
            }
        }
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            mc.ingameGUI.chatGUI.resetScroll()
            val text = chatInput.textField
            chat.sendMessage(text.value)
            text.value = chat.defaultInputFieldText

            if (!TabbyChatClient.settings.advanced.keepChatOpen.value) {
                mc.displayGuiScreen(null)
            }
            return true
        }

        if (chat.field_228174_e_.func_228115_a_(keyCode, scanCode, modifiers)) {
            return true
        }
        if (keyCode == GLFW.GLFW_KEY_TAB) {
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        if (chat.field_228174_e_.func_228113_a_(x, y, button)) {
            return true
        }
        if (button == 0 && (tray.location.contains(x, y) || Screen.hasAltDown() && location.contains(x, y))) {
            dragMode = !tray.isHandleHovered(x, y)
            drag = Vec2i(x.toInt(), y.toInt())
            tempbox = location.copy()
        }
        return super.mouseClicked(x, y, button)
    }

    private fun handleDragging(mx: Double, my: Double) {
        if (drag != null) {
            if (!dragMode) {
                location = Location(
                        tempbox!!.xPos,
                        tempbox!!.yPos + my.toInt() - drag!!.y,
                        tempbox!!.width + mx.toInt() - drag!!.x,
                        tempbox!!.height - my.toInt() + drag!!.y)
                ChatManager.markDirty(activeChannel)
            } else {
                val loc = location.copy()
                loc.xPos = tempbox!!.xPos + mx.toInt() - drag!!.x
                loc.yPos = tempbox!!.yPos + my.toInt() - drag!!.y
                location = loc.asImmutable()
            }
        }
    }

    override fun mouseReleased(x: Double, y: Double, button: Int): Boolean {
        if (drag != null) {
            drag = null
            tempbox = null
        }
        return super.mouseReleased(x, y, button)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        return this.chat.field_228174_e_.func_228112_a_(scroll) || super.mouseScrolled(x, y, scroll)
    }

    private fun normalizeLocation(bounds: ILocation): ILocation {
        val scale = mc.gameSettings.chatScale

        // original dims
        val x = (bounds.xPos * scale).toInt()
        val y = (bounds.yPos * scale).toInt()
        val w = (bounds.width * scale).toInt()
        val h = (bounds.height * scale).toInt()

        // the new dims
        var w1 = w
        var h1 = h
        var x1 = x
        var y1 = y

        val screenW = mc.func_228018_at_().scaledWidth
        val screenH = mc.func_228018_at_().scaledHeight

        val hotbar = 25

        // limits for sizes
        val minW = 50
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
            return Location(
                    MathHelper.ceil(x1 / scale),
                    MathHelper.ceil(y1 / scale),
                    MathHelper.ceil(w1 / scale),
                    MathHelper.ceil(h1 / scale))
        }

        return bounds
    }

    override fun onClosed() {
        super.onClosed()
        tick()
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

        val bottom = oldDim.scaledHeight - location.yPos
        val y = newDim.scaledHeight - bottom
        val loc = location.copy()
        loc.yPos = y
        this.location = loc
        this.tick()
    }

    private class TCRect(private val parent: Rectangle2d) : Rectangle2d(0, 0, 0, 0) {

        override fun getX(): Int {
            return max(0, min(parent.x + location.xPos, chat.width - parent.width))
        }

        override fun getY(): Int {
            return location.yHeight - parent.height - 14 * chatInput.wrappedLines.size
        }

        override fun getWidth(): Int {
            return parent.width
        }

        override fun getHeight(): Int {
            return parent.height
        }

        override fun contains(x: Int, y: Int): Boolean {
            return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height
        }
    }

}
