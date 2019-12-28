package mnm.mods.tabbychat.client.extra.filters

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.component.*
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Translation
import mnm.mods.tabbychat.util.toComponent
import net.minecraft.client.resources.I18n
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundEvent
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fml.common.registry.GameRegistry
import net.minecraftforge.registries.ForgeRegistries
import org.lwjgl.glfw.GLFW

class GuiFilterEditor(private val filters: List<UserFilter>, private val filter: UserFilter) : GuiPanel() {

    private val txtName: GuiText
    private val chkRemove: GuiCheckbox
    private val txtDestinations: GuiText
    private val chkSound: GuiCheckbox
    private val txtSound: GuiText
    private val txtPattern: GuiText
    private val lblError: GuiLabel

    private val btnRegexp: ToggleButton
    private val btnIgnoreCase: ToggleButton
    private val btnRaw: ToggleButton

    internal inner class ToggleButton(text: String) : AbstractGuiButton() {

        var toggle = false

        override val text: String = text
            get() {
                val color = if (toggle) TextFormatting.GREEN else TextFormatting.RED
                return color.toString() + field
            }

        override fun onPress() {
            toggle = !toggle
        }
    }

    init {
        this.layout = GuiGridLayout(20, 15)

        val pattern = filter.rawPattern
        val settings = filter.settings

        var pos = 0

        this.add(GuiLabel(Translation.FILTER_NAME.toComponent()), intArrayOf(1, pos))
        txtName = this.add(GuiText(), intArrayOf(5, pos, 10, 1)) {
            value = filter.name
        }

        pos += 2
        this.add(GuiLabel(Translation.FILTER_DESTINATIONS.toComponent()), intArrayOf(1, pos))
        txtDestinations = this.add(GuiText(), intArrayOf(8, pos, 10, 1)) {
            value = settings.channels.joinToString(", ")
//            caption = Translation.FILTER_DESTIONATIONS_DESC.toComponent()
        }

        pos += 1
        btnRegexp = this.add(ToggleButton(".*"), intArrayOf(1, pos, 2, 1)) {
            toggle = filter.settings.isRegex
//            caption = Translation.FILTER_REGEX.toComponent()
        }
        btnIgnoreCase = this.add(ToggleButton("Aa"), intArrayOf(3, pos, 2, 1)) {
            toggle = settings.isCaseInsensitive
//            caption = Translation.FILTER_IGNORE_CASE.toComponent()
        }
        btnRaw = this.add(ToggleButton("&0"), intArrayOf(5, pos, 2, 1)) {
            toggle = settings.isRaw
//            caption = Translation.FILTER_RAW_INPUT.toComponent()
        }

        pos += 2
        this.add(GuiLabel(Translation.FILTER_HIDE.toComponent()), intArrayOf(2, pos))
        chkRemove = this.add(GuiCheckbox(), intArrayOf(1, pos)) {
            value = settings.isRemove
        }

        pos += 1
        this.add(GuiLabel(Translation.FILTER_AUDIO_NOTIFY.toComponent()), intArrayOf(2, pos))
        chkSound = this.add(GuiCheckbox(), intArrayOf(1, pos)) {
            value = settings.isSoundNotification
        }

        pos += 1
        val play = this.add(GuiButton("\u25b6"), intArrayOf(18, pos, 2, 1))
        txtSound = this.add<GuiText>(object : GuiText() {
            private var index: Int = 0

            override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
                val max = 10
                if (keyCode == GLFW.GLFW_KEY_DOWN) {
                    index++
                } else if (keyCode == GLFW.GLFW_KEY_UP) {
                    index--
                }
                // suggest sounds
                val value = value.toLowerCase().substring(0, delegate.cursorPosition)
                var list = GameRegistry.findRegistry(SoundEvent::class.java).keys.asSequence()
                        .map { it.toString() }
                        .filter { s -> s.contains(value) }
                        .toList()

                index = index.coerceAtMost(list.size - max)
                index = index.coerceAtLeast(0)
                if (list.size > max) {
                    list = list.subList(index, index + max)
                }
                hint = list.joinToString("\n")

                if ((keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) && list.isNotEmpty()) {
                    this.value = list[0]
                    setFocused(null)
                }
                return super.keyPressed(keyCode, scanCode, modifiers)
            }
        }, intArrayOf(3, pos, 14, 1)) {
            value = settings.soundName ?: ""
            delegate.maxStringLength = 100
            delegate.setValidator { txt -> ResourceLocation.tryCreate(txt) != null }
            delegate.setResponder { s ->
                val res = ResourceLocation.tryCreate(s)
                play.sound = ForgeRegistries.SOUND_EVENTS.getValue(res)
            }
        }
        lblError = GuiLabel()

        pos += 2
        this.add(GuiLabel(Translation.FILTER_EXPRESSION.toComponent()), intArrayOf(1, pos))
        txtPattern = this.add(GuiText(), intArrayOf(8, pos, 12, 1)) {
            value = pattern
            delegate.setResponder {
                primaryColor = Color.WHITE
                lblError.text = null
                if (btnRegexp.active) {
                    // check valid regex
                    try {
                        filter.testPattern(it)
                    } catch (e: UserFilter.UserPatternException) {
                        primaryColor = Color.RED
                        lblError.text = e.cause?.localizedMessage?.toComponent()
                    }

                }
            }
        }

        pos++
        this.add(lblError, intArrayOf(4, pos))

        this.add(GuiButton(I18n.format("gui.done")) {
            accept()
        }, intArrayOf(5, 14, 4, 1))
    }

    private fun accept() {
        filter.apply {
            name = txtName.value
            setPattern(txtPattern.value)
            settings.apply {
                channels = txtDestinations.value.split(",").filter { it.isNotBlank() }.toMutableList()
                isRemove = chkRemove.value
                isCaseInsensitive = btnIgnoreCase.toggle
                isRegex = btnRegexp.toggle
                isRaw = btnRaw.toggle

                isSoundNotification = chkSound.value
                soundName = txtSound.value
            }
        }
        TabbyChatClient.serverSettings.setFilters(filters)
    }
}
