package mnm.mods.tabbychat.client.gui.settings
//
//import com.mojang.blaze3d.systems.RenderSystem
//import mnm.mods.tabbychat.client.ChannelImpl
//import mnm.mods.tabbychat.client.TabbyChatClient
//import mnm.mods.tabbychat.client.gui.ChatBox
//import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
//import mnm.mods.tabbychat.client.gui.widget.FieldBackedCheckboxButton
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.client.gui.Label
//import mnm.mods.tabbychat.client.gui.component.GuiPanel
//import mnm.mods.tabbychat.client.settings.ServerSettings
//import mnm.mods.tabbychat.util.*
//import net.minecraft.client.Minecraft
//import net.minecraft.client.gui.FocusableGui
//import net.minecraft.client.gui.IGuiEventListener
//import net.minecraft.client.gui.IRenderable
//import net.minecraft.client.gui.widget.TextFieldWidget
//import net.minecraft.client.gui.widget.button.Button
//import net.minecraft.client.gui.widget.list.AbstractList
//
//class GuiSettingsChannel(private val channelName: String? = null) : SettingPanel<ServerSettings> {
//
//    private lateinit var channels: GuiScrollingPanel
//    private lateinit var panel: GuiPanel
//
//    private lateinit var optAlias: TextFieldWidget
//    private lateinit var optPrefix: TextFieldWidget
//    private lateinit var optHidePrefix: FieldBackedCheckboxButton
//    private lateinit var optCommand: TextFieldWidget
//
//    override val settings: ServerSettings = TabbyChatClient.serverSettings
//
//    private var channel: ChannelImpl? = null
//    private val configChannels = settings.channels
//
//    private var channelConfig: ChannelConfig? = null
//
//
//    override fun init(screen: IScreenHelper) {
//        channels = add(GuiScrollingPanel(), BorderLayout.Position.WEST).apply {
//            location = Location(0, 0, 60, 200)
//            contentPanel.apply {
//                layout = VerticalLayout()
//                for (channel in configChannels) {
//                    if (channel.name == channelName) {
//                        this@GuiSettingsChannel.channel = channel
//                    }
//                    add(ChannelButton(channel)) {
//                        location = Location(0, 0, 60, 15)
//                    }
//                }
//            }
//        }
//
//        panel = this.add(GuiPanel(), BorderLayout.Position.CENTER).apply {
//            layout = GuiGridLayout(8, 20)
//        }
//
//        this.select(channel)
//    }
//
//    class ChannelConfig : FocusableGui(), IRenderable {
//        val renderers = ArrayList<IRenderable>()
//        val children = ArrayList<IGuiEventListener>()
//
//        override fun render(p0: Int, p1: Int, p2: Float) {
//            renderers.forEach {
//                it.render(p0, p1, p2)
//            }
//        }
//
//        override fun children(): MutableList<out IGuiEventListener> {
//            return children
//        }
//    }
//
//    private fun select(channel: ChannelImpl?) {
//
//        for (comp in channels.contentPanel.children()) {
//            comp.active = (comp as ChannelButton).channel !== channel
//        }
//
//        var pos = 1
//
//        this.channel = channel
//        this.panel.clear()
//        if (channel == null) {
//            if (channels.contentPanel.children().isNotEmpty()) {
//                this.panel.add(Label(Translation.CHANNEL_SELECT), intArrayOf(1, pos))
//            } else {
//                this.panel.add(Label(Translation.CHANNEL_NONE), intArrayOf(1, pos))
//            }
//            return
//        }
//        this.panel.add(Label(mc.fontRenderer, Translation.CHANNEL_LABEL.toComponent(channel.name), ), intArrayOf(1, pos))
//
//        pos += 3
//        this.panel.add(Label(Translation.CHANNEL_ALIAS.toComponent()), intArrayOf(1, pos))
//        optAlias = this.panel.add(TextFieldWidget(), intArrayOf(3, pos, 4, 1)).apply {
//            value = channel.alias
//        }
//
//        pos += 2
//        this.panel.add(Label(Translation.CHANNEL_PREFIX.toComponent()), intArrayOf(1, pos))
//        optPrefix = this.panel.add(TextFieldWidget(), intArrayOf(3, pos, 4, 1)).apply {
//            value = channel.prefix
//        }
//
//        pos += 2
//        optHidePrefix = this.panel.add(FieldBackedCheckboxButton(), intArrayOf(1, pos)).apply {
//            value = channel.isPrefixHidden
//        }
//        this.panel.add(Label(Translation.CHANNEL_HIDE_PREFIX.toComponent()), intArrayOf(2, pos))
//
//        pos += 2
//        this.panel.add(Label(Translation.CHANNEL_COMMAND.toComponent()), intArrayOf(1, pos))
//        optCommand = this.panel.add(TextFieldWidget(), intArrayOf(3, pos, 4, 1)).apply {
//            value = channel.command
//        }
//
//        this.panel.add(Button(I18n.format("gui.done")) {
//            save()
//        }, intArrayOf(2, 15, 4, 2))
//
//        this.panel.add(Button(Translation.CHANNEL_FORGET.translate()) {
//
//            // remove from chat
//            ChatBox.removeChannel(channel)
//            // remove from settings file
//            configChannels.remove(channel)
//            // don't add this channel again.
//            settings.general.ignoredChannels += channel.toString()
//            // remove from settings gui
//            for (comp in this.channels.contentPanel.children()) {
//                if (comp is ChannelButton && comp.channel === channel) {
//                    this.channels.contentPanel.remove(comp)
//                    break
//                }
//            }
//            select(null)
//        }, intArrayOf(2, 17, 4, 2))
//    }
//
//    private fun save() {
//        channel?.apply {
//            alias = optAlias.value
//            prefix = optPrefix.value
//            isPrefixHidden = optHidePrefix.isChecked
//            command = optCommand.value
//        }
//    }
//
//    private inner class ScrollingChannelPane(mc: Minecraft, w: Int, h: Int, top: Int, bottom: Int, itemHeight: Int)
//        : AbstractList<ScrollingChannelPane.Entry>(mc, w, h, top, bottom, itemHeight) {
//
//        fun add(channel: ChannelImpl) {
//            this.addEntry(Entry(channel))
//        }
//
//        private inner class Entry(val channel: ChannelImpl) : AbstractList.AbstractListEntry<Entry>() {
//            override fun render(row: Int, top: Int, left: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, hovered: Boolean, partialTicks: Float) {
//                RenderSystem.pushMatrix()
//
//                if (hovered) {
//                    fill(left, top, left + width, top + height, Colors.GRAY.with(a = 0xaa))
//                }
//
//                val font = minecraft.fontRenderer
//                font.drawString(font.trimStringToWidth(channel.displayName, width), left.toFloat(), top.toFloat(), -1)
//
//                RenderSystem.popMatrix()
//            }
//        }
//    }
//}
