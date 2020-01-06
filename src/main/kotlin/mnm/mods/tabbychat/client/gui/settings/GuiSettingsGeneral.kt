package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.component.GuiLabel
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingBoolean
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingEnum
import mnm.mods.tabbychat.client.gui.component.config.GuiSettingNumber.GuiSettingDouble
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
import mnm.mods.tabbychat.client.settings.TabbySettings
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.TimeStamps
import mnm.mods.tabbychat.util.Translatable
import mnm.mods.tabbychat.util.Translation
import net.minecraft.util.text.TextFormatting
import java.text.NumberFormat

internal class GuiSettingsGeneral : SettingPanel<TabbySettings>() {

    private val colors: Array<TextFormatting> = TextFormatting.values().filter { it.isColor }.toTypedArray()

    override val settings: TabbySettings = TabbyChatClient.settings

    override val displayString: String by Translation.SETTINGS_GENERAL

    init {
        layout = GuiGridLayout(10, 20)
        secondaryColor = Color(255, 0, 255, 64)
    }

    override fun initGUI() {
        var pos = 1
        add(GuiLabel(Translation.LOG_CHAT.toComponent()), intArrayOf(2, pos))
        add(GuiSettingBoolean(settings.general::logChat), intArrayOf(1, pos)).apply {
            //            caption = Translation.LOG_CHAT_DESC.toComponent()
        }

        add(GuiLabel(Translation.SPLIT_LOG.toComponent()), intArrayOf(7, pos))
        add(GuiSettingBoolean(settings.general::splitLog), intArrayOf(6, pos)).apply {
            //            caption = Translation.SPLIT_LOG_DESC.toComponent()
        }

        pos += 2
        add(GuiLabel(Translation.TIMESTAMP.toComponent()), intArrayOf(2, pos))
        add(GuiSettingBoolean(settings.general::timestampChat), intArrayOf(1, pos))

        pos += 2
        add(GuiLabel(Translation.TIMESTAMP_STYLE.toComponent()), intArrayOf(3, pos))
        add(GuiSettingEnum.of(settings.general::timestampStyle, TimeStamps.values()), intArrayOf(5, pos, 4, 1))

        pos += 2
        add(GuiLabel(Translation.TIMESTAMP_COLOR.toComponent()), intArrayOf(3, pos))
        add(GuiSettingEnum(settings.general::timestampColor, colors) { Translatable { "colors.$friendlyName" } }, intArrayOf(5, pos, 4, 1))

        pos += 2
        add(GuiLabel(Translation.ANTI_SPAM.toComponent()), intArrayOf(2, pos))
        add(GuiSettingBoolean(settings.general::antiSpam), intArrayOf(1, pos)).apply {
            // caption = Translation.ANTI_SPAM_DESC.toComponent()
        }

        pos += 2
        add(GuiLabel(Translation.SPAM_PREJUDICE.toComponent()), intArrayOf(3, pos))
        add(GuiSettingDouble(settings.general::antiSpamPrejudice), intArrayOf(6, pos, 2, 1)).apply {
            delegate.apply {
                min = 0.0
                max = 1.0
                interval = 0.05
                format = NumberFormat.getPercentInstance()
            }
            // caption = Translation.SPAM_PREJUDICE_DESC.toComponent()
        }

        pos += 2
        add(GuiLabel(Translation.UNREAD_FLASHING.toComponent()), intArrayOf(2, pos))
        add(GuiSettingBoolean(settings.general::unreadFlashing), intArrayOf(1, pos))

        pos += 2
        add(GuiLabel(Translation.CHECK_UPDATES.toComponent()), intArrayOf(2, pos))
        add(GuiSettingBoolean(settings.general::checkUpdates), intArrayOf(1, pos))
    }

}
