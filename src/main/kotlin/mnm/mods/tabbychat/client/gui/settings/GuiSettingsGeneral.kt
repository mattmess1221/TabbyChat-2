package mnm.mods.tabbychat.client.gui.settings
//
//import mnm.mods.tabbychat.client.TabbyChatClient
//import mnm.mods.tabbychat.client.gui.widget.DoubleUpDownWidget
//import mnm.mods.tabbychat.client.gui.widget.FieldBackedCheckboxButton
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.client.gui.Label
//import mnm.mods.tabbychat.client.gui.widget.EnumValues
//import mnm.mods.tabbychat.client.gui.widget.SelectWidget
//import mnm.mods.tabbychat.client.settings.TabbySettings
//import mnm.mods.tabbychat.util.TimeStamps
//import mnm.mods.tabbychat.util.Translation
//import mnm.mods.tabbychat.util.mc
//import mnm.mods.tabbychat.util.translate
//import net.minecraft.util.text.TextFormatting
//import net.minecraft.util.text.TranslationTextComponent
//import java.text.NumberFormat
//
//class GuiSettingsGeneral : SettingPanel<TabbySettings> {
//
//    private val colors: Array<TextFormatting> = TextFormatting.values().filter { it.isColor }.toTypedArray()
//
//    override val settings: TabbySettings = TabbyChatClient.settings
//
//    override fun init(screen: IScreenHelper) {
//        val left = screen.width / 2 - 80
//        fun top(indx: Int) = indx * 25 + 30
//
//        var pos = 0
////        screen.addRender(Label(mc.fontRenderer, Translation.LOG_CHAT, left, top(pos)))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::logChat,
//                left + 60, top(pos), 80, 20,
//                Translation.LOG_CHAT.translate())) {
//            //            caption = Translation.LOG_CHAT_DESC.toComponent()
//        }
//
//        pos++
////        add(Label(Translation.SPLIT_LOG.toComponent()), intArrayOf(7, pos))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::splitLog,
//                left + 60, top(pos), 80, 20,
//                Translation.SPLIT_LOG.translate())) {
//            //            caption = Translation.SPLIT_LOG_DESC.toComponent()
//        }
//
//        pos++
////        add(Label(Translation.TIMESTAMP.toComponent()), intArrayOf(2, pos))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::timestampChat,
//                left + 60, top(pos), 80, 20,
//                Translation.TIMESTAMP.translate()))
//
//        pos++
//        screen.addRender(Label(mc.fontRenderer, Translation.TIMESTAMP_STYLE, left, top(pos)))
//        screen.addWidget(SelectWidget(
//                settings.general::timestampStyle, EnumValues(TimeStamps.values()),
//                left + 80, top(pos), 80, 20, ""))
//
//        pos++
//        screen.addRender(Label(mc.fontRenderer, Translation.TIMESTAMP_COLOR, left, top(pos)))
//        screen.addWidget(SelectWidget(settings.general::timestampColor,
//                EnumValues(colors) { TranslationTextComponent("colors.${it.friendlyName}") },
//                left + 80, top(pos), 80, 20, ""))
//
//        pos++
//        screen.addRender(Label(mc.fontRenderer, Translation.ANTI_SPAM, left, top(pos)))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::antiSpam,
//                left + 80, top(pos), 80, 20,
//                Translation.ANTI_SPAM
//        )) {
//            // caption = Translation.ANTI_SPAM_DESC.toComponent()
//        }
//
//        pos++
//        screen.addRender(Label(mc.fontRenderer, Translation.SPAM_PREJUDICE, left, top(pos)))
//        screen.addWidget(DoubleUpDownWidget(settings.general::antiSpamPrejudice,
//                screen.width / 2 + 20, 25 * pos + 10, 80, 20)) {
//            minValue = 0.0
//            maxValue = 1.0
//            interval = 0.05
//            format = NumberFormat.getPercentInstance()
//            // caption = Translation.SPAM_PREJUDICE_DESC.toComponent()
//        }
//
//        pos++
////        screen.addRender(Label(mc.fontRenderer, Translation.UNREAD_FLASHING, left, top(pos)))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::unreadFlashing,
//                left + 60, top(pos), 80, 20,
//                Translation.UNREAD_FLASHING.formattedText))
//
//        pos++
////        add(Label(Translation.CHECK_UPDATES.toComponent()), intArrayOf(2, pos))
//        screen.addWidget(FieldBackedCheckboxButton(settings.general::checkUpdates,
//                left + 60, top(pos), 80, 20, Translation.CHECK_UPDATES.formattedText))
//    }
//
//}
