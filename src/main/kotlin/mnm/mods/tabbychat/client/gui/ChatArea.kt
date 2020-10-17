package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.ChatMessage
import mnm.mods.tabbychat.client.DefaultChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.util.*
import net.minecraft.client.gui.*
import net.minecraft.client.gui.screen.Screen
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import java.util.*

class ChatArea(private val chatbox: ChatBox)
    : AbstractGui(), IGuiEventListener, IRenderable, ILocatable {

   override val xPos get() = chatbox.xPos
   override val yPos get() = chatbox.yPos + chatbox.tray.height
   override val width get() = chatbox.width
   override val height get() = chatbox.height - chatbox.tray.height - chatbox.chatInput.height

    internal var channel: Channel = DefaultChannel

    var scrollPos = 0
        set(scroll) {
            val size = chat.size - mc.ingameGUI.chatGUI.lineCount
            field = if (size < 0 || scroll < 0) {
                0
            } else if (scroll > size) {
                size
            } else {
                scroll
            }
        }

    internal val chat: List<ChatMessage>
        get() = ChatManager.getVisible(channel, width - 6)

    private val visibleChat: List<ChatMessage>
        get() {
            val lines = chat

            val messages = ArrayList<ChatMessage>()
            var length = 0

            var pos = scrollPos
            val unfoc = TabbyChatClient.settings.advanced.unfocHeight
            val div = if (mc.ingameGUI.chatGUI.chatOpen) 1.toFloat() else unfoc
            while (pos < lines.size && length < height * div - 10) {
                val line = lines[pos]

                if (mc.ingameGUI.chatGUI.chatOpen) {
                    messages.add(line)
                } else if (getLineOpacity(line) > 3) {
                    messages.add(line)
                } else {
                    break
                }

                pos++
                length += mc.fontRenderer.FONT_HEIGHT
            }

            return messages
        }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        // One tick = 120
        if (this.xPos < x && this.yPos < y && this.xPos + this.width > x && this.yPos + this.height > y && scroll != 0.0) {
            val s = scroll.coerceIn(-1.0, 1.0)
            scroll((if (Screen.hasShiftDown()) s * 7 else s).toInt())

            return true
        }
        return false
    }

    /*override TODO*/ fun onClosed() {
        resetScroll()
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        val visible = visibleChat
        val chatOpen = mc.ingameGUI.chatGUI.chatOpen
        if (visible.isNotEmpty() || chatOpen) {
            RenderSystem.enableBlend()
            val opac = mc.gameSettings.chatOpacity.toFloat()
            RenderSystem.color4f(1f, 1f, 1f, opac)

            val height = if (chatOpen) this.height else visible.size * mc.fontRenderer.FONT_HEIGHT
            val minY = this.y2 - height
            drawModalCorners(xPos, minY, width, height, MODAL)

            blitOffset = 100
            // TODO abstracted padding
            val xPos = this.x1 + 3
            var yPos = this.y2
            for (line in visible) {
                yPos -= mc.fontRenderer.FONT_HEIGHT
                drawChatLine(line, xPos, yPos)
            }
            blitOffset = 0
            RenderSystem.disableAlphaTest()
            RenderSystem.disableBlend()
        }
    }

    private fun drawChatLine(line: ChatMessage, xPos: Int, yPos: Int) {
        val text = ChatTextUtils.getMessageWithOptionalTimestamp(line).formattedText
        mc.fontRenderer.drawStringWithShadow(text, xPos.toFloat(), yPos.toFloat(), Colors.WHITE + (getLineOpacity(line) shl 24))
    }

    @Deprecated("")
    fun markDirty() {
        ChatManager.markDirty(channel)
    }

    private fun getLineOpacity(line: ChatMessage): Int {
        val vis = TabbyChatClient.settings.advanced.visibility
        when {
            vis === LocalVisibility.ALWAYS -> return 4
            vis === LocalVisibility.HIDDEN && !mc.ingameGUI.chatGUI.chatOpen -> return 0
            else -> {
                var opacity = (mc.gameSettings.chatOpacity * 255).toInt()

                val age = (mc.ingameGUI.ticks - line.counter).toDouble()
                if (!mc.ingameGUI.chatGUI.chatOpen) {
                    var opacPerc = age / TabbyChatClient.settings.advanced.fadeTime
                    opacPerc = 1.0 - opacPerc
                    opacPerc *= 10.0

                    opacPerc = opacPerc.coerceIn(0.0..1.0)

                    opacPerc *= opacPerc
                    opacity = (opacity * opacPerc).toInt()
                }
                return opacity
            }
        }
    }

    fun scroll(scr: Int) {
        scrollPos += scr
    }

    fun resetScroll() {
        scrollPos = 0
    }

    fun getTextComponent(clickX: Int, clickY: Int): ITextComponent? {
        if (mc.ingameGUI.chatGUI.chatOpen) {
            val scale = mc.ingameGUI.chatGUI.scale
            return getTextComponentClamped(
                    MathHelper.floor(clickX / scale),
                    MathHelper.floor(clickY / scale),
                    scale)
        }
        return null
    }

    private fun getTextComponentClamped(clickX: Int, clickY: Int, scale: Double): ITextComponent? {

        // check that cursor is in bounds.
        if (clickX in xPos..(xPos+width) && clickY in yPos..(yPos + height)) {
            val size = mc.fontRenderer.FONT_HEIGHT * scale
            val bottom = (yPos + height).toDouble()

            // The line to get
            val linePos = MathHelper.floor((clickY - bottom) / -size) + this.scrollPos

            // Iterate through the chat component, stopping when the desired
            // x is reached.
            val list = this.chat
            if (linePos >= 0 && linePos < list.size) {
                val chatline = list[linePos]
                var x = (xPos + 3).toFloat()

                for (ichatcomponent in ChatTextUtils.getMessageWithOptionalTimestamp(chatline)) {
                    if (ichatcomponent is StringTextComponent) {

                        // get the text of the component, no children.
                        val text = ichatcomponent.unformattedComponentText
                        // clean it up
                        val clean = RenderComponentsUtil.removeTextColorsIfConfigured(text, false)
                        // get it's width, then scale it.
                        x += (mc.fontRenderer.getStringWidth(clean) * scale).toFloat()

                        if (x > clickX) {
                            return ichatcomponent
                        }
                    }
                }
            }
        }

        return null
    }

    companion object {
        private val MODAL = TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 205)
    }
}
