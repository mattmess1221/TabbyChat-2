package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.client.AbstractChannel
import mnm.mods.tabbychat.client.ChatChannel
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.gui.ChatBox
import mnm.mods.tabbychat.client.gui.component.*
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
import mnm.mods.tabbychat.client.gui.component.layout.VerticalLayout
import mnm.mods.tabbychat.client.settings.ServerSettings
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.Translation
import net.minecraft.client.resources.I18n

internal class GuiSettingsChannel(private val channelName: String? = null) : SettingPanel<ServerSettings>() {

    private lateinit var channels: GuiScrollingPanel
    private lateinit var panel: GuiPanel

    private lateinit var optAlias: GuiText
    private lateinit var optPrefix: GuiText
    private lateinit var optHidePrefix: GuiCheckbox
    private lateinit var optCommand: GuiText

    override val displayString by Translation.CHANNEL_TITLE

    override val settings: ServerSettings = TabbyChatClient.serverSettings

    private var channel: AbstractChannel? = null
    val configChannels = settings.getChannels().toMutableList()

    init {
        this.layout = BorderLayout()
        this.secondaryColor = Color(0, 15, 100, 65)
    }

    override fun initGUI() {
        channels = add(GuiScrollingPanel(), BorderLayout.Position.WEST).apply {
            location = Location(0, 0, 60, 200)
            contentPanel.apply {
                layout = VerticalLayout()
                for (channel in configChannels) {
                    if (channel.name == channelName) {
                        this@GuiSettingsChannel.channel = channel
                    }
                    add(ChannelButton(channel)) {
                        location = Location(0, 0, 60, 15)
                    }
                }
            }
        }

        panel = this.add(GuiPanel(), BorderLayout.Position.CENTER).apply {
            layout = GuiGridLayout(8, 20)
        }

        this.select(channel)
    }

    private fun select(channel: AbstractChannel?) {

        for (comp in channels.contentPanel.children()) {
            comp.active = (comp as ChannelButton).channel !== channel
        }

        var pos = 1

        this.channel = channel
        this.panel.clear()
        if (channel == null) {
            if (channels.contentPanel.children().isNotEmpty()) {
                this.panel.add(GuiLabel(Translation.CHANNEL_SELECT.toComponent()), intArrayOf(1, pos))
            } else {
                this.panel.add(GuiLabel(Translation.CHANNEL_NONE.toComponent()), intArrayOf(1, pos))
            }
            return
        }
        this.panel.add(GuiLabel(Translation.CHANNEL_LABEL.toComponent(channel.name)), intArrayOf(1, pos))

        pos += 3
        this.panel.add(GuiLabel(Translation.CHANNEL_ALIAS.toComponent()), intArrayOf(1, pos))
        optAlias = this.panel.add(GuiText(), intArrayOf(3, pos, 4, 1)).apply {
            value = channel.alias
        }

        pos += 2
        this.panel.add(GuiLabel(Translation.CHANNEL_PREFIX.toComponent()), intArrayOf(1, pos))
        optPrefix = this.panel.add(GuiText(), intArrayOf(3, pos, 4, 1)).apply {
            value = channel.prefix
        }

        pos += 2
        optHidePrefix = this.panel.add(GuiCheckbox(), intArrayOf(1, pos)).apply {
            value = channel.isPrefixHidden
        }
        this.panel.add(GuiLabel(Translation.CHANNEL_HIDE_PREFIX.toComponent()), intArrayOf(2, pos))

        pos += 2
        this.panel.add(GuiLabel(Translation.CHANNEL_COMMAND.toComponent()), intArrayOf(1, pos))
        optCommand = this.panel.add(GuiText(), intArrayOf(3, pos, 4, 1)).apply {
            value = channel.command
        }

        this.panel.add(GuiButton(I18n.format("gui.done")) {
            save()
        }, intArrayOf(2, 15, 4, 2))

        this.panel.add(GuiButton(Translation.CHANNEL_FORGET.translate()) {

            // remove from chat
            ChatBox.removeChannel(channel)
            // remove from settings file
            configChannels.remove(channel)
            // don't add this channel again.
            settings.general.ignoredChannels.value = settings.general.ignoredChannels.value.toMutableSet().also {it.add(channel.toString())}.toList()
            // remove from settings gui
            for (comp in this.channels.contentPanel.children()) {
                if (comp is ChannelButton && comp.channel === channel) {
                    this.channels.contentPanel.remove(comp)
                    break
                }
            }
            select(null)
            settings.setChannels(configChannels)
        }, intArrayOf(2, 17, 4, 2))
    }

    private fun save() {

        channel?.apply {
            alias = optAlias.value
            prefix = optPrefix.value
            isPrefixHidden = optHidePrefix.value
            command = optCommand.value
        }

        settings.setChannels(configChannels)
    }

    private inner class ChannelButton internal constructor(internal val channel: ChatChannel) : AbstractGuiButton() {

        override val text: String = channel.name

        override fun onPress() {
            select(channel)
        }
    }
}
