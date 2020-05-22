package mnm.mods.tabbychat.client.gui

import com.mojang.blaze3d.systems.RenderSystem
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.component.GuiText
import mnm.mods.tabbychat.client.gui.component.GuiWrappedComponent
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Dim
import mnm.mods.tabbychat.util.TexturedModal
import mnm.mods.tabbychat.util.mc
import mnm.mods.tabbychat.util.text.FancyFontRenderer
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.util.text.ITextComponent
import kotlin.math.max
import kotlin.math.min

object TextBox : GuiWrappedComponent<GuiText>(GuiText(
        object : TextFieldWidget(mc.fontRenderer, 0, 0, 0, 0, "input") {
            // Dummy textField
            override fun render(x: Int, y: Int, parTicks: Float) {
                // noop
            }

            override fun setSuggestion(p_195612_1_: String?) {
                TextBox.suggestion = p_195612_1_
                super.setSuggestion(p_195612_1_)
            }
        })) {

    private val MODAL = TexturedModal(ChatBox.GUI_LOCATION, 0, 219, 254, 37)

    private val fr = mc.fontRenderer
    private var cursorCounter: Int = 0
    private val spellcheck = TabbyChatClient.spellcheck

    var textFormatter: (String, Int) -> String = { text, _ -> text }
    var suggestion: String? = null

    val wrappedLines: List<String>
        get() = fr.listFormattedStringToWidth(delegate.value, location.width)

    private val formattedLines: List<ITextComponent>
        get() {
            val spelling = spellcheck.checkSpelling(text)
            val formatter: (String, Int) -> ITextComponent? = { line, len ->
                spelling(textFormatter(line, len))
            }
            val lines = ArrayList<ITextComponent>()
            var length = 0
            for (line in wrappedLines) {
                formatter(line, length)?.also {
                    lines.add(it)
                    length += line.length
                }
            }
            return lines
        }

    override var minimumSize: Dim
        get() = Dim(100, (fr.FONT_HEIGHT + 2) * wrappedLines.size)
        set(_) {
        }

    var text: String
        get() = delegate.value
        set(text) {
            delegate.value = text
        }

    override var visible: Boolean
        get() = mc.ingameGUI.chatGUI.chatOpen
        set(_) {
        }

    init {
        delegate.delegate.maxStringLength = ChatManager.MAX_CHAT_LENGTH
        delegate.delegate.setCanLoseFocus(false)
        delegate.delegate.setEnableBackgroundDrawing(false)
        delegate.delegate.setFocused2(true)
    }

    override fun onClosed() {
        this.text = ""
        super.onClosed()
    }

    override fun render(x: Int, y: Int, parTicks: Float) {
        RenderSystem.enableBlend()
        drawModalCorners(MODAL)
        RenderSystem.disableBlend()

        drawText()
        drawCursor()

    }

    private fun drawCursor() {
        val textField = this.delegate.delegate
        val rawText = StringBuilder(this.text)

        // keeps track of all the characters. Used to compensate for spaces
        var totalPos = 0

        // The current pixel row. adds FONT_HEIGHT each iteration
        var line = 0

        // The position of the cursor
        var pos = textField.cursorPosition
        // the position of the selection
        val sel = pos + textField.selectedText.length

        // make the position and selection in order
        var start = min(pos, sel)
        var end = max(pos, sel)

        val loc = location

        for (text in wrappedLines) {

            // cursor drawing
            if (pos >= 0 && pos <= text.length) {
                // cursor is on this line
                val c = fr.getStringWidth(text.substring(0, pos))
                val cursorBlink = this.cursorCounter / 6 % 3 != 0
                if (cursorBlink) {
                    if (textField.cursorPosition < this.delegate.value.length) {
                        vLine(loc.xPos + c + 3,
                                loc.yPos + line - 2,
                                loc.yPos + line + fr.FONT_HEIGHT + 1, -0x2f2f30)
                    } else {
                        fr.drawString("_", (loc.xPos + c + 2).toFloat(), (loc.yPos + line + 1).toFloat(), primaryColorProperty.hex)
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
                x = fr.getStringWidth(text.substring(0, start))
            }

            // test the end
            if (end >= 0 && end <= text.length) {
                w = fr.getStringWidth(text.substring(max(start, 0), end)) + 2
            }

            val lineY = line + fr.FONT_HEIGHT + 2

            if (w != 0) {
                if (x >= 0 && w > 0) {
                    // start and end on same line
                    drawSelectionBox(x + 2, line, x + w, lineY)
                } else {
                    if (x >= 0) {
                        // started on this line
                        drawSelectionBox(x + 2, line, x + fr.getStringWidth(text.substring(start)) + 1, lineY)
                    }
                    if (w >= 0) {
                        // ends on this line
                        drawSelectionBox(2, line, w, lineY)
                    }
                    if (start < 0 && end > text.length) {
                        // full line
                        drawSelectionBox(1, line, fr.getStringWidth(text), lineY)
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
        val ffr = FancyFontRenderer(fr)
        val loc = location
        var xPos = loc.xPos + 3
        var yPos = loc.yPos + 1
        val lines = formattedLines
        for (line in lines) {
            val color = Color.WHITE
            xPos = loc.xPos + 3
            ffr.drawChat(line, xPos.toFloat(), yPos.toFloat(), color.hex, false)
            yPos += fr.FONT_HEIGHT + 2
            xPos += fr.getStringWidth(line.string)
        }
        yPos -= fr.FONT_HEIGHT + 2

        val flag2 = delegate.delegate.cursorPosition < text.length || text.length >= delegate.delegate.maxStringLength

        if (!flag2 && suggestion != null) {
            this.fr.drawStringWithShadow(this.suggestion!!, xPos.toFloat(), yPos.toFloat(), -8355712)
        }

    }

    /**
     * Draws the blue selection box. Forwards to [TextFieldWidget.drawSelectionBox]
     */
    private fun drawSelectionBox(x1: Int, y1: Int, x2: Int, y2: Int) {
        this.delegate.delegate.drawSelectionBox(
                x1 + location.xPos, y1 + location.yPos,
                x2 + location.xPos, y2 + location.yPos)
    }

    override fun tick() {
        this.cursorCounter++
    }

    override fun mouseClicked(x: Double, y: Double, button: Int): Boolean {
        if (button == 0) {
            val bounds = this.location

            val width = bounds.width - 1
            val row = y.toInt() / (fr.FONT_HEIGHT + 2)

            val lines = wrappedLines
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
            index += fr.trimStringToWidth(lines[row], x.toInt() - 3).length
            delegate.delegate.cursorPosition = index
            return true
        }
        return false
    }
}
