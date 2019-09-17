package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.platform.GlStateManager
import mnm.mods.tabbychat.client.AbstractChannel
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.ChatMessage
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.component.GuiComponent
import mnm.mods.tabbychat.util.*
import net.minecraft.client.gui.RenderComponentsUtil
import net.minecraft.client.gui.screen.Screen
import net.minecraft.entity.player.ChatVisibility
import net.minecraft.util.math.MathHelper
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import java.util.ArrayList

class ChatArea : GuiComponent() {

    internal var channel: AbstractChannel? = null

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

    override var location: ILocation
        get() {
            val height = visibleChat.size * mc.fontRenderer.FONT_HEIGHT
            val vis = TabbyChatClient.settings.advanced.visibility.value

            if (mc.ingameGUI.chatGUI.chatOpen || vis === LocalVisibility.ALWAYS) {
                return super.location
            }
            if (height != 0) {
                return super.location.copy().apply {
                    move(0, super.location.height - height - 2)
                    this.height = height + 2
                }.asImmutable()
            }
            return super.location
        }
        set(value) {
            super.location = value
        }

    override var visible: Boolean
        get() {
            val height = visibleChat.size * mc.fontRenderer.FONT_HEIGHT
            val vis = TabbyChatClient.settings.advanced.visibility.value

            return mc.gameSettings.chatVisibility != ChatVisibility.HIDDEN && (mc.ingameGUI.chatGUI.chatOpen || vis === LocalVisibility.ALWAYS || height != 0)
        }
        set(value) {
            super.visible = value
        }

    internal val chat: List<ChatMessage>
        get() = ChatManager.getVisible(channel!!, super.location.width - 6)

    private val visibleChat: List<ChatMessage>
        get() {
            val lines = chat

            val messages = ArrayList<ChatMessage>()
            var length = 0

            var pos = scrollPos
            val unfoc = TabbyChatClient.settings.advanced.unfocHeight.value
            val div = if (mc.ingameGUI.chatGUI.chatOpen) 1.toFloat() else unfoc
            while (pos < lines.size && length < super.location.height * div - 10) {
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

    init {
        this.minimumSize = Dim(300, 160)
    }

    override fun mouseScrolled(x: Double, y: Double, scroll: Double): Boolean {
        // One tick = 120
        if (location.contains(x, y) && scroll != 0.0) {
            val s = scroll.coerceIn(-1.0, 1.0)
            scroll((if (Screen.hasShiftDown()) s * 7 else s).toInt())

            return true
        }
        return false
    }

    override fun onClosed() {
        resetScroll()
        super.onClosed()
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        val visible = visibleChat
        GlStateManager.enableBlend()
        val opac = mc.gameSettings.chatOpacity.toFloat()
        GlStateManager.color4f(1f, 1f, 1f, opac)

        drawModalCorners(MODAL)

        blitOffset = 100
        // TODO abstracted padding
        val xPos = location.xPos + 3
        var yPos = location.yHeight
        for (line in visible) {
            yPos -= mc.fontRenderer.FONT_HEIGHT
            drawChatLine(line, xPos, yPos)
        }
        blitOffset = 0
        GlStateManager.disableAlphaTest()
        GlStateManager.disableBlend()
    }

    private fun drawChatLine(line: ChatMessage, xPos: Int, yPos: Int) {
        val text = ChatTextUtils.getMessageWithOptionalTimestamp(line).formattedText
        mc.fontRenderer.drawStringWithShadow(text, xPos.toFloat(), yPos.toFloat(), Color.WHITE.hex + (getLineOpacity(line) shl 24))
    }

    @Deprecated("")
    fun markDirty() {
        ChatManager.markDirty(channel)
    }

    private fun getLineOpacity(line: ChatMessage): Int {
        val vis = TabbyChatClient.settings.advanced.visibility.value
        when {
            vis === LocalVisibility.ALWAYS -> return 4
            vis === LocalVisibility.HIDDEN && !mc.ingameGUI.chatGUI.chatOpen -> return 0
            else -> {
                var opacity = (mc.gameSettings.chatOpacity * 255).toInt()

                val age = (mc.ingameGUI.ticks - line.counter).toDouble()
                if (!mc.ingameGUI.chatGUI.chatOpen) {
                    var opacPerc = age / TabbyChatClient.settings.advanced.fadeTime.value
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
        var clickX = clickX
        var clickY = clickY
        if (mc.ingameGUI.chatGUI.chatOpen) {
            val scale = mc.ingameGUI.chatGUI.scale
            clickX = MathHelper.floor(clickX / scale)
            clickY = MathHelper.floor(clickY / scale)

            val actual = location
            // check that cursor is in bounds.
            if (actual.contains(clickX, clickY)) {


                val size = mc.fontRenderer.FONT_HEIGHT * scale
                val bottom = (actual.yPos + actual.height).toDouble()

                // The line to get
                val linePos = MathHelper.floor((clickY - bottom) / -size) + this.scrollPos

                // Iterate through the chat component, stopping when the desired
                // x is reached.
                val list = this.chat
                if (linePos >= 0 && linePos < list.size) {
                    val chatline = list[linePos]
                    var x = (actual.xPos + 3).toFloat()

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
        }
        return null
    }

    companion object {
        private val MODAL = TexturedModal(ChatBox.GUI_LOCATION, 0, 14, 254, 205)
    }
}
