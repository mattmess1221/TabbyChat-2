package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.CONFIG
import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.api.Channel
import mnm.mods.tabbychat.client.AbstractChannel
import mnm.mods.tabbychat.client.ChatManager
import mnm.mods.tabbychat.client.DefaultChannel
import mnm.mods.tabbychat.client.gui.component.ComponentScreen
import mnm.mods.tabbychat.client.gui.component.GuiButton
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.client.gui.component.layout.VerticalLayout
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.mc
import net.minecraft.client.Minecraft
import net.minecraft.util.text.StringTextComponent
import kotlin.reflect.KClass

class GuiSettingsScreen(channel: Channel?) : ComponentScreen(StringTextComponent("Settings")) {

    private val panels = mutableListOf<SettingPanel<*>>()

    private lateinit var settingsPanel: GuiPanel

    private lateinit var settingsList: GuiPanel
    private var selectedSetting: SettingPanel<*>? = null

    init {
        if (channel !== DefaultChannel) {
            selectedSetting = GuiSettingsChannel(channel as AbstractChannel?)
        }

        for ((key, value) in settings) {
            try {
                if (selectedSetting != null && selectedSetting!!.javaClass == key) {
                    panels.add(selectedSetting!!)
                } else {
                    panels.add(value())
                }
            } catch (e: Exception) {
                TabbyChat.logger.error(CONFIG, "Unable to add {} as a setting.", key, e)
            }

        }
    }

    public override fun init() {

        settingsPanel = panel.add(GuiPanel(BorderLayout()), null)

        val x = this.width / 2 - 300 / 2
        val y = this.height / 2 - 200 / 2
        settingsPanel.location = Location(x, y, 300, 200)

        val panel = GuiPanel(BorderLayout())
        this.settingsPanel.add(panel, BorderLayout.Position.WEST)
        settingsList = panel.add(GuiPanel(VerticalLayout()), BorderLayout.Position.WEST)

        panel.add(GuiButton("Close") {
            mc.displayGuiScreen(null)
        }, BorderLayout.Position.SOUTH).apply {
            location = Location(0, 0, 40, 10)
            secondaryColor = Color(0, 255, 0, 127)
        }

        // Populate the settings
        for (sett in panels) {
            val button = SettingsButton(sett) {
                selectSetting(sett)
            }
            settingsList.add(button)
            sett.initGUI()
        }
        selectSetting(selectedSetting ?: panels[0])
    }

    override fun onClose() {
        super.onClose()
        for (settingPanel in panels) {
            settingPanel.settings.save()
        }
        ChatManager.markDirty(null)
    }

    override fun init(mc: Minecraft, width: Int, height: Int) {
        this.panels.forEach { it.clear() }
        super.init(mc, width, height)
    }

    private fun deactivateAll() {
        for (comp in settingsList.children()) {
            if (comp is SettingsButton) {
                comp.active = false
            }
        }
    }

    private fun <T : SettingPanel<*>> activate(settingClass: Class<T>) {
        for (comp in settingsList.children()) {
            if (comp is SettingsButton && comp.settings.javaClass == settingClass) {
                comp.active = true
                break
            }
        }
    }

    override fun render(mouseX: Int, mouseY: Int, tick: Float) {
        // drawDefaultBackground();
        val rect = settingsPanel.location
        fill(rect.xPos, rect.yPos, rect.xWidth, rect.yHeight, Integer.MIN_VALUE)
        super.render(mouseX, mouseY, tick)
    }

    private fun selectSetting(setting: SettingPanel<*>) {
        //        setting.clearComponents();
        deactivateAll()
        selectedSetting?.apply { settingsPanel.remove(this) }
        selectedSetting = settingsPanel.add(setting, BorderLayout.Position.CENTER) {
            activate(this.javaClass)
        }

    }

    companion object {
        private val settings: Map<KClass<out SettingPanel<*>>, () -> SettingPanel<*>> = mapOf(
                GuiSettingsGeneral::class to { GuiSettingsGeneral() },
                GuiSettingsServer::class to { GuiSettingsServer() },
                GuiSettingsChannel::class to { GuiSettingsChannel() },
                GuiSettingsFilters::class to { GuiSettingsFilters() },
                GuiAdvancedSettings::class to { GuiAdvancedSettings() }
        )
    }
}
