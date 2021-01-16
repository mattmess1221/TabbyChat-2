package mnm.mods.tabbychat.client.gui.widget

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.gui.ChatBox
import mnm.mods.tabbychat.client.gui.drawModalCorners
import mnm.mods.tabbychat.extra.spell.Spellcheck
import mnm.mods.tabbychat.util.Colors
import mnm.mods.tabbychat.util.TexturedModal
import mnm.mods.tabbychat.util.mc
import mnm.mods.tabbychat.util.text.FancyFontRenderer
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.IRenderable
import net.minecraft.client.gui.RenderComponentsUtil
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.util.text.ITextComponent
import kotlin.math.max
import kotlin.math.min

class MultiLineTextFieldWidget(
        private val chat: ChatBox,
        private val font: FontRenderer,
        text: String
) : TextFieldWidget(font, ChatBox.xPos,
        ChatBox.yPos + ChatBox.height - font.FONT_HEIGHT,
        ChatBox.width, font.FONT_HEIGHT, text),
        IRenderable {

    private val MODAL = TexturedModal(ChatBox.GUI_LOCATION, 0, 219, 254, 37)

    private var cursorCounter: Int = 0

    // TODO make this a callback instead of a hard dependency
    private val spellcheck = Spellcheck

    var textFormatter: (String) -> ITextComponent = spellcheck::checkSpelling

    val lines: List<String>
        get() = font.listFormattedStringToWidth(text, width)

    private val displayLines: List<ITextComponent>
        get() = RenderComponentsUtil.splitText(textFormatter(text), width, mc.fontRenderer, false, true)

    init {
        maxStringLength = ChatManager.MAX_CHAT_LENGTH
        setCanLoseFocus(false)
        setEnableBackgroundDrawing(false)
        setFocused2(true)
        updateLocation()
    }

    fun updateLocation() {
        width = chat.width
        height = lines.size * (font.FONT_HEIGHT + 2)
        x = chat.xPos
        y = chat.yPos + ChatBox.height - height
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        updateLocation()

        RenderSystem.enableBlend()
        drawModalCorners(this.x, this.y, width, height, MODAL)
        RenderSystem.disableBlend()

        drawText()
        drawCursor()
    }

    private fun drawCursor() {
        val rawText = StringBuilder(this.text)

        // keeps track of all the characters. Used to compensate for spaces
        var totalPos = 0

        // The current pixel row. adds FONT_HEIGHT each iteration
        var line = 0

        // The position of the cursor
        var pos = this.cursorPosition
        // the position of the selection
        val sel = this.selectionEnd

        // make the position and selection in order
        var start = min(pos, sel)
        var end = max(pos, sel)

        for (text in lines) {

            // cursor drawing
            if (pos >= 0 && pos <= text.length) {
                // cursor is on this line
                val str = text.substring(0, pos)
                // if the line ends with a non-rendered space, compensate
                val offset = if (str.endsWith(" ")) 1 else 0
                val c = font.getStringWidth(str) - offset
                val cursorBlink = this.cursorCounter / 6 % 3 != 0
                if (cursorBlink) {
                    if (this.cursorPosition < this.text.length) {
                        vLine(x + c + 3,
                                y + line - 2,
                                y + line + font.FONT_HEIGHT + 1, -0x2f2f30)
                    } else {
                        font.drawString("_", (x + c + 2).toFloat(), (y + line + 1).toFloat(), -1)
                    }

                }
            }

            // selection highlighting

            // the start of the highlight.
            var x = -1
            // the end of the highlight.
            var w = -1

            // test the start
            if (start >= 0 && start <= text.length) {
                x = this.x + font.getStringWidth(text.substring(0, start))
            }

            // test the end
            if (end >= 0 && end <= text.length) {
                w = font.getStringWidth(text.substring(max(start, 0), end)) + 2
            }

            val lineY = line + font.FONT_HEIGHT + 2

            if (start != end) {
                if (x >= 0 && w > 0) {
                    // start and end on same line
                    drawSelectionBox(x + 2, y + line, x + w + 2, y + lineY)
                } else {
                    if (x >= 0) {
                        // started on this line
                        drawSelectionBox(x + 2, y + line, x + font.getStringWidth(text.substring(start)) + 2, y + lineY)
                    }
                    if (w >= 0) {
                        // ends on this line
                        drawSelectionBox(this.x + 2, y + line, this.x + w + 2, y + lineY)
                    }
                    if (start < 0 && end > text.length) {
                        // full line
                        drawSelectionBox(this.x + 2, y + line, this.x + font.getStringWidth(text) + 2, y + lineY)
                    }
                }
            }

            // keep track of the lines
            totalPos += text.length

            // prepare all the markers for the next line.
            pos -= text.length
            start -= text.length
            end -= text.length

            // the string splitter already strips out spaces, so I need to
            // follow along with the raw string, removing any spaces I see.
            rawText.delete(0, text.length)
            while (rawText.isNotEmpty() && rawText[0] == ' ') {
                // compensate for spaces
                pos--
                start--
                end--
                totalPos++
                rawText.deleteCharAt(0)
            }
            line = lineY
        }

    }

    private fun drawText() {
        val ffr = FancyFontRenderer(font)
        var xPos = x + 3
        var yPos = y + 1
        val lines = displayLines
        for (line in lines) {
            val color = Colors.WHITE
            xPos = x + 3
            ffr.drawChat(line, xPos.toFloat(), yPos.toFloat(), color, false)
            yPos += font.FONT_HEIGHT + 2
            xPos += font.getStringWidth(line.string)
        }
        yPos -= font.FONT_HEIGHT + 2

        val flag2 = cursorPosition < text.length || text.length >= maxStringLength

        if (!flag2 && suggestion != null) {
            this.font.drawStringWithShadow(this.suggestion, xPos.toFloat(), yPos.toFloat(), -8355712)
        }

    }

//    /**
//     * Draws the blue selection box. Forwards to [TextFieldWidget.drawSelectionBox]
//     */
//    override fun drawSelectionBox(x1: Int, y1: Int, x2: Int, y2: Int) {
//        this.delegate.delegate.drawSelectionBox(
//                x1 + location.xPos, y1 + location.yPos,
//                x2 + location.xPos, y2 + location.yPos)
//    }

    override fun tick() {
        this.cursorCounter++
    }

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        if (button == 0) {

            val width = width - 1
            val row = y.toInt() / (font.FONT_HEIGHT + 2)

            val lines = lines
            if (row < 0 || row >= lines.size || x < 0 || x > width) {
                return false
            }
            var index = 0
            for (i in 0 until row) {
                index += lines[i].length
                // check for spaces because trailing spaces are trimmed
                if (text[index] == ' ') {
                    index++
                }
            }
            index += font.trimStringToWidth(lines[row], x.toInt() - 3).length
            cursorPosition = index
            return true
        }
        return false
    }
}
