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
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.Vec2i
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.IGuiEventListener
import net.minecraft.client.gui.screen.ChatScreen
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.renderer.Rectangle2d
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.MathHelper
import net.minecraftforge.common.MinecraftForge
import java.util.*
import java.util.function.Predicate
import kotlin.math.max
import kotlin.math.min

object ChatBox : GuiPanel(BorderLayout()) {

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
                    text.textField.textField.text = channel.prefix + " "
                }
            }
            // set max text length
            val hidden = channel.isPrefixHidden
            val prefLength = if (hidden) channel.prefix.length + 1 else 0

            text.textField.textField.maxStringLength = ChatManager.MAX_CHAT_LENGTH - prefLength

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

    override// save bounds
    var location: ILocation = super.location
        set(location) {
            normalizeLocation(location).also {
                if (location != location) {
                    field = location
                    val sett = TabbyChatClient.settings
                    sett.advanced.chatboxLocation = location
                    sett.save()
                }
            }

        }

    init {
        tray = this.add(ChatTray(), BorderLayout.Position.NORTH)
        chatArea = this.add(ChatArea(), BorderLayout.Position.CENTER)
        chatInput = this.add(TextBox, BorderLayout.Position.SOUTH)
        this.add(Scrollbar(chatArea), BorderLayout.Position.EAST)

        super.location = TabbyChatClient.settings.advanced.chatboxLocation

        this.channels.add(DefaultChannel)
        this.tray.addChannel(DefaultChannel)

        status[DefaultChannel] = ChannelStatus.ACTIVE

        super.tick()

        MinecraftForge.EVENT_BUS.addListener(::messageScroller)
        MinecraftForge.EVENT_BUS.addListener(::addChatMessage)
    }

    fun update(chat: ChatScreen) {
        this.chat = chat
        if (chat.suggestions != null && chat.suggestions.field_198505_b !is TCRect) {
            chat.suggestions.field_198505_b = TCRect(chat.suggestions.field_198505_b)
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
        this.channels.removeIf(Predicate.isEqual<Any>(DefaultChannel).negate())

        this.tray.clearMessages()
        status[DefaultChannel] = ChannelStatus.ACTIVE
    }

    private val settings get() = TabbyChatClient.serverSettings!!

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
            Minecraft.getInstance().player.sendChatMessage(cmd)
        }
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        handleDragging(mouseX.toDouble(), mouseY.toDouble())

        super.render(mouseX, mouseY, parTicks)
        if (mc.ingameGUI.chatGUI.chatOpen) {
            val fr = Minecraft.getInstance().fontRenderer
            val loc = location
            val height = fr.FONT_HEIGHT + 3
            val xPos = chat.commandUsagePosition + loc.xPos
            var yPos = loc.yHeight - chat.commandUsage.size * height
            when {
                chat.suggestions != null -> chat.suggestions.render(mouseX, mouseY)
                xPos + chat.commandUsageWidth > loc.xWidth -> {

                    for ((i, s) in chat.commandUsage.withIndex()) {
                        fill(0,
                                chat.height - 14 - 12 * i,
                                chat.commandUsageWidth + 1,
                                chat.height - 2 - 12 * i,
                                -0x1000000)
                        fr.drawStringWithShadow(s, 1f, (chat.height - 14 + 2 - 12 * i).toFloat(), -1)
                    }
                }
                else -> for (s in chat.commandUsage) {
                    fill(xPos - 1, yPos, xPos + chat.commandUsageWidth + 1, yPos - height, -0x30000000)
                    fr.drawStringWithShadow(s, xPos.toFloat(), (yPos - height + 2).toFloat(), -1)
                    yPos += height
                }
            }

            val itextcomponent = mc.ingameGUI.chatGUI.getTextComponent(mouseX.toDouble(), mouseY.toDouble())
            if (itextcomponent != null && itextcomponent.style.hoverEvent != null) {
                chat.renderComponentHoverEffect(itextcomponent, mouseX, mouseY)
            }
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0 && (tray.location.contains(mouseX, mouseY) || Screen.hasAltDown() && location.contains(mouseX, mouseY))) {
            dragMode = !tray.isHandleHovered(mouseX, mouseY)
            drag = Vec2i(mouseX.toInt(), mouseY.toInt())
            tempbox = location.copy()
        }
        return super.mouseClicked(mouseX, mouseY, button)
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

    override fun mouseReleased(x: Double, y: Double, b: Int): Boolean {
        if (drag != null) {
            drag = null
            tempbox = null
        }
        return super.mouseReleased(x, y, b)
    }

    override fun mouseScrolled(p_mouseScrolled_1_: Double, p_mouseScrolled_3_: Double, p_mouseScrolled_5_: Double): Boolean {
        return this.chatArea.mouseScrolled(p_mouseScrolled_1_, p_mouseScrolled_3_, p_mouseScrolled_5_)
    }

    private fun normalizeLocation(bounds: ILocation): ILocation {
        var bounds = bounds
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

        val screenW = mc.mainWindow.scaledWidth
        val screenH = mc.mainWindow.scaledHeight

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
            bounds = Location(
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
