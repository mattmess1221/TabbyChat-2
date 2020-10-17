package mnm.mods.tabbychat.client.gui.settings
//
//import mnm.mods.tabbychat.client.TabbyChatClient
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.client.gui.Label
//import mnm.mods.tabbychat.client.gui.component.*
//import mnm.mods.tabbychat.client.gui.widget.*
//import mnm.mods.tabbychat.client.settings.ServerSettings
//import mnm.mods.tabbychat.util.*
//import net.minecraft.client.gui.widget.TextFieldWidget
//
//internal class GuiSettingsServer : SettingPanel<ServerSettings> {
//
//    override val settings = TabbyChatClient.serverSettings
//
//    override fun init(screen: IScreenHelper) {
//
//        val left = screen.width / 2 - 80
//        fun top(pos: Int) = pos * 25 + 10
//
//        var pos = 0
////        this.add(Label(Translation.CHANNELS_ENABLED.toComponent()), intArrayOf(2, pos))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::channelsEnabled,
//                left + 60, top(pos), 80, 20,
//                Translation.CHANNELS_ENABLED.translate())) {
//            //            caption = Translation.CHANNELS_ENABLED_DESC.toComponent()
//        }
//
//        pos += 1
////        this.add(Label(Translation.PM_ENABLED.toComponent()), intArrayOf(2, pos))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::pmEnabled,
//                left + 60, top(pos), 80, 20,
//                Translation.PM_ENABLED.translate())) {
//            //            caption = Translation.PM_ENABLED_DESC.toComponent()
//        }
//
//        pos += 1
////        this.add(Label(Translation.USE_DEFAULT.toComponent()), intArrayOf(2, pos))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::useDefaultTab,
//                left + 60, top(pos), 80, 20,
//                Translation.USE_DEFAULT.translate()))
//
//        pos += 1
//        screen.addRender(Label(mc.fontRenderer, Translation.CHANNEL_PATTERN, left, top(pos)))
//        screen.addWidget(SelectWidget(settings.general::channelPattern, EnumValues(ChannelPatterns.values()) { it.display },
//                left + 80, top(pos), 80, 20, "")) {
//            //            caption = Translation.CHANNEL_PATTERN_DESC.toComponent()
//        }
//
//        pos += 1
//        screen.addRender(Label(mc.fontRenderer, Translation.MESSAGE_PATTERN, left, top(pos)))
//        screen.addWidget(SelectWidget(settings.general::messegePattern, EnumValues(MessagePatterns.values()) { it.display },
//                left + 80, top(pos), 80, 20, "")) {
//            //            caption = Translation.MESSAGE_PATTERN_DESC.toComponent()
//        }
//
//        pos += 1
//        screen.addRender(Label(mc.fontRenderer, Translation.IGNORED_CHANNELS, left, top(pos)))
//        screen.addWidget(TextFieldWidget(mc.fontRenderer, left + 80, top(pos), 120, 20, "")) {
//            setResponder { text ->
//                settings.general.ignoredChannels = text.split(",")
//                        .map { it.trim() }
//                        .filter { it.isNotEmpty() }
//            }
//            //            caption = Translation.IGNORED_CHANNELS_DESC.toComponent()
//        }
//
//        // TODO
////        pos += 1
////        this.add(GuiLabel(Translation.DEFAULT_CHANNEL_COMMAND.toComponent()), intArrayOf(0, pos))
////        this.add(GuiSettingString(settings.general::channelCommand), intArrayOf(5, pos, 5, 1)).apply {
////            caption = Translation.DEFAULT_CHANNEL_COMMAND_DESC.toComponent()
////        }
//
//        pos += 2
//        screen.addRender(Label(mc.fontRenderer, Translation.DEFAULT_CHANNEL, left, top(pos)))
//        screen.addWidget(FieldBackedTextFieldWidget(settings.general::defaultChannel,
//                mc.fontRenderer, left + 80, top(pos), 80, 20,
//                Translation.DEFAULT_CHANNEL.translate())) {
//
//            //            caption = Translation.DEFAULT_CHANNEL_DESC.toComponent()
//        }
//    }
//
//}
