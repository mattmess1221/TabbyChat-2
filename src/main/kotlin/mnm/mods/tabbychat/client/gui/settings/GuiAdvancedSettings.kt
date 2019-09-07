package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.settings.TabbySettings
import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
import mnm.mods.tabbychat.client.gui.component.GuiLabel
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingBoolean
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingEnum
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingInt
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel
import mnm.mods.tabbychat.util.Translation

class GuiAdvancedSettings : SettingPanel<TabbySettings>() {

    override val settings: TabbySettings = TabbyChatClient.settings
    override val displayString by Translation.SETTINGS_ADVANCED

    init {
        layout = GuiGridLayout(10, 15)
        secondaryColor = Color(255, 0, 0, 64)
    }

    override fun initGUI() {
        add(GuiSettingInt(settings.advanced.fadeTime), intArrayOf(5, 1, 2, 1)).apply {
            delegate.interval = 50.0
        }

        add(GuiLabel(Translation.ADVANCED_CHAT_VISIBILITY.toComponent()), intArrayOf(1, 3))
        add(GuiSettingEnum.of(settings.advanced.visibility, LocalVisibility.values()), intArrayOf(5, 3, 3, 1))

        add(GuiLabel(Translation.ADVANCED_HIDE_DELIMS.toComponent()), intArrayOf(2, 5))
        add(GuiSettingBoolean(settings.advanced.hideTag), intArrayOf(1, 5))

        add(GuiLabel(Translation.ADVANCED_SPELLCHECK.toComponent()), intArrayOf(2, 6))
        add(GuiSettingBoolean(settings.advanced.spelling), intArrayOf(1, 6))

        add(GuiLabel(Translation.EXPERIMENTAL.toComponent()), intArrayOf(0, 13))
    }
}
