package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.client.AbstractChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.api.ChannelStatus
import mnm.mods.tabbychat.client.gui.settings.GuiSettingsScreen
import mnm.mods.tabbychat.client.gui.component.GuiButton
import mnm.mods.tabbychat.util.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.screen.Screen

class ChatTab internal constructor(private val channel: AbstractChannel) : GuiButton(channel.displayName) {

    init {
        val chan = "<${channel.displayName}>"
        minimumSize = Dim(mc.fontRenderer.getStringWidth(chan) + 8, 14)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (location.contains(mouseX, mouseY)) {
            if (button == 0) {
                if (Screen.hasShiftDown()) {
                    // Remove channel
                    ChatBox.removeChannel(this.channel)
                } else {
                    // Enable channel, disable others
                    ChatBox.activeChannel = this.channel
                }
            } else if (button == 1) {
                // Open channel options
                openSettings()
            } else if (button == 2) {
                // middle click
                ChatBox.removeChannel(this.channel)
            } else {
                return false
            }
            return true
        }
        return false
    }

    private fun openSettings() {
        Minecraft.getInstance().displayGuiScreen(GuiSettingsScreen(channel))
    }

    override fun render(mouseX: Int, mouseY: Int, parTicks: Float) {
        val status = ChatBox.status[channel]
        if (mc.ingameGUI.chatGUI.chatOpen
                || status != null && status > ChannelStatus.PINGED && TabbyChatClient.settings.general.unreadFlashing.value
                || TabbyChatClient.settings.advanced.visibility.value === LocalVisibility.ALWAYS) {
            val loc = location
            GlStateManager.enableBlend()
            GlStateManager.color4f(1f, 1f, 1f, mc.gameSettings.chatOpacity.toFloat())
            drawModalCorners(getStatusModal(loc.contains(mouseX, mouseY)))

            val txtX = loc.xCenter
            val txtY = loc.yCenter - 2

            val (red, green, blue) = primaryColorProperty
            val color = Color.getHex(red, green, blue, (mc.gameSettings.chatOpacity * 255).toInt())
            this.drawCenteredString(mc.fontRenderer, this.text, txtX, txtY, color)
            GlStateManager.disableBlend()
        }
    }

    override var text: String
        get() {
            val alias = channel.displayName

            return when (ChatBox.status[channel]) {
                ChannelStatus.ACTIVE -> "[$alias]"
                ChannelStatus.UNREAD -> "<$alias>"
                else -> alias
            }
        }
        set(value) {
            super.text = value
        }

    private fun getStatusModal(hovered: Boolean): TexturedModal {
        if (hovered) {
            return HOVERED
        }
        return when (ChatBox.status[channel]) {
            ChannelStatus.ACTIVE -> ACTIVE
            ChannelStatus.UNREAD -> UNREAD
            ChannelStatus.PINGED -> PINGED
            else -> NONE
        }
    }

    companion object {

        private val ACTIVE = TexturedModal(ChatBox.GUI_LOCATION, 0, 0, 50, 14)
        private val UNREAD = TexturedModal(ChatBox.GUI_LOCATION, 50, 0, 50, 14)
        private val PINGED = TexturedModal(ChatBox.GUI_LOCATION, 100, 0, 50, 14)
        private val HOVERED = TexturedModal(ChatBox.GUI_LOCATION, 150, 0, 50, 14)
        private val NONE = TexturedModal(ChatBox.GUI_LOCATION, 200, 0, 50, 14)
    }
}
