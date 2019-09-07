package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.component.GuiLabel
import mnm.mods.tabbychat.client.gui.component.config.*
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
import mnm.mods.tabbychat.client.settings.ServerSettings
import mnm.mods.tabbychat.util.ChannelPatterns
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.MessagePatterns
import mnm.mods.tabbychat.util.Translation

internal class GuiSettingsServer : SettingPanel<ServerSettings>() {

    override val displayString: String by Translation.SETTINGS_SERVER
    override val settings = TabbyChatClient.serverSettings!!

    init {
        this.layout = GuiGridLayout(10, 20)
        this.secondaryColor = Color(255, 215, 0, 64)
    }

    override fun initGUI() {
        val sett = settings.general

        var pos = 1
        this.add(GuiLabel(Translation.CHANNELS_ENABLED.toComponent()), intArrayOf(2, pos))
        this.add(GuiSettingBoolean(sett.channelsEnabled), intArrayOf(1, pos)).apply {
            caption = Translation.CHANNELS_ENABLED_DESC.toComponent()
        }

        pos += 1
        this.add(GuiLabel(Translation.PM_ENABLED.toComponent()), intArrayOf(2, pos))
        this.add(GuiSettingBoolean(sett.pmEnabled), intArrayOf(1, pos)).apply {
            caption = Translation.PM_ENABLED_DESC.toComponent()
        }

        pos += 1
        this.add(GuiLabel(Translation.USE_DEFAULT.toComponent()), intArrayOf(2, pos))
        this.add(GuiSettingBoolean(sett.useDefaultTab), intArrayOf(1, pos))

        pos += 2
        this.add(GuiLabel(Translation.CHANNEL_PATTERN.toComponent()), intArrayOf(1, pos))
        this.add(GuiSettingEnum(sett.channelPattern, ChannelPatterns.values()), intArrayOf(5, pos, 4, 1)).apply {
            caption = Translation.CHANNEL_PATTERN_DESC.toComponent()
        }

        pos += 2
        this.add(GuiLabel(Translation.MESSAGE_PATTERN.toComponent()), intArrayOf(1, pos))
        this.add(GuiSettingEnum(sett.messegePattern, MessagePatterns.values()), intArrayOf(5, pos, 4, 1)).apply {
            caption = Translation.MESSAGE_PATTERN_DESC.toComponent()
        }

        pos += 2
        this.add(GuiLabel(Translation.IGNORED_CHANNELS.toComponent()), intArrayOf(0, pos))
        this.add(GuiSettingStringList(sett.ignoredChannels), intArrayOf(5, pos, 5, 1)).apply {
            caption = Translation.IGNORED_CHANNELS_DESC.toComponent()
        }

        pos += 2
        this.add(GuiLabel(Translation.DEFAULT_CHANNEL_COMMAND.toComponent()), intArrayOf(0, pos))
        this.add(GuiSettingString(sett.channelCommand), intArrayOf(5, pos, 5, 1)).apply {
            caption = Translation.DEFAULT_CHANNEL_COMMAND_DESC.toComponent()
        }

        pos += 2
        this.add(GuiLabel(Translation.DEFAULT_CHANNEL.toComponent()), intArrayOf(0, pos))
        this.add(GuiSettingString(sett.defaultChannel), intArrayOf(5, pos, 5, 1)).apply {
            caption = Translation.DEFAULT_CHANNEL_DESC.toComponent()
        }
    }

}
