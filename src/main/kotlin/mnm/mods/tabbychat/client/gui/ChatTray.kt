package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.api.ChannelStatus
import mnm.mods.tabbychat.client.AbstractChannel
import mnm.mods.tabbychat.client.DefaultChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.client.gui.component.layout.FlowLayout
import mnm.mods.tabbychat.util.*
import mnm.mods.tabbychat.util.config.Value

class ChatTray internal constructor() : GuiPanel() {

    private val tabList: GuiPanel
    private val handle: GuiComponent = ChatHandle()

    private val map = HashMap<Channel, GuiComponent>()

    override var minimumSize: Dim
        get() = tabList.layout?.layoutSize ?: super.minimumSize
        set(value) {
            super.minimumSize = value
        }

    init {
        layout = BorderLayout()
        minimumSize = Dim(40, 20)
        tabList = this.add(GuiPanel(), BorderLayout.Position.CENTER) {
            layout = FlowLayout()
        }
        this.add(ChatPanel(), BorderLayout.Position.EAST) {
            layout = FlowLayout()
            add(ToggleButton())
            add(handle)
        }
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        if (mc.ingameGUI.chatGUI.chatOpen) {
            GlStateManager.enableBlend()
            GlStateManager.color4f(1f, 1f, 1f, mc.gameSettings.chatOpacity.toFloat())
            drawModalCorners(MODAL)
            GlStateManager.disableBlend()
        }
        super.render(x, y, parTicks)
    }

    override fun tick() {
        super.tick()
        val panel = parent
        if (panel != null) {
            val (red, green, blue, alpha) = panel.secondaryColorProperty
            secondaryColor = Color(red, green, blue, alpha / 4 * 3)
        }
    }

    fun addChannel(channel: AbstractChannel) {
        tabList.add(ChatTab(channel)) {
            map[channel] = this
        }
    }

    fun removeChannel(channel: Channel) {
        if (channel in map) {
            this.tabList.remove(map.remove(channel)!!)
        }
    }

    fun clearMessages() {
        this.tabList.clear()

        addChannel(DefaultChannel)
        ChatBox.status[DefaultChannel] = ChannelStatus.ACTIVE
    }

    internal fun isHandleHovered(x: Double, y: Double): Boolean {
        return handle.location.contains(x, y)
    }

    private class ToggleButton internal constructor() : GuiComponent() {

        private val value: Value<Boolean> = TabbyChatClient.settings.advanced.keepChatOpen

        override var location: ILocation
            get() = super.location.copy().move(0, 2)
            set(value) {
                super.location = value
            }

        override var minimumSize: Dim
            get() = Dim(8, 8)
            set(value) {
                super.minimumSize = value
            }

        override fun render(x: Int, y: Int, parTicks: Float) {
            GlStateManager.enableBlend()
            val loc = location
            val opac = (mc.gameSettings.chatOpacity * 255).toInt() shl 24
            renderBorders(loc.xPos + 2, loc.yPos + 2, loc.xWidth - 2, loc.yHeight - 2, 0x999999 or opac)
            if (value.value) {
                fill(loc.xPos + 3, loc.yPos + 3, loc.xWidth - 3, loc.yHeight - 3, 0xaaaaaa or opac)
            }
        }

        override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
            if (location.contains(x, y) && button == 0) {
                value.value = !value.value
                return true
            }
            return false
        }
    }

    companion object {
        private val MODAL = TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 202)
    }
}
