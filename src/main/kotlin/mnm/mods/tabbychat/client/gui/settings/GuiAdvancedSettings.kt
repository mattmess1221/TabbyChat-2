package mnm.mods.tabbychat.client.gui.settings
//
//import mnm.mods.tabbychat.client.TabbyChatClient
//import mnm.mods.tabbychat.client.gui.widget.FieldBackedCheckboxButton
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.client.gui.widget.IntUpDownWidget
//import mnm.mods.tabbychat.client.gui.Label
//import mnm.mods.tabbychat.client.gui.widget.EnumValues
//import mnm.mods.tabbychat.client.gui.widget.SelectWidget
//import mnm.mods.tabbychat.client.settings.TabbySettings
//import mnm.mods.tabbychat.util.*
//
//class GuiAdvancedSettings : SettingPanel<TabbySettings> {
//
//    override val settings: TabbySettings = TabbyChatClient.settings
//
//    override fun init(screen: IScreenHelper) {
//        screen.addRender(Label(mc.fontRenderer, Translation.ADVANCED_FADE_TIME,
//                screen.width / 2 - 40, screen.height / 2 - 60
//        ))
//        screen.addWidget(IntUpDownWidget(settings.advanced::fadeTime, screen.width / 2 + 10, screen.height / 2 - 60, 40, 16)) {
//            interval = 50
//        }
//
//        screen.addRender(Label(mc.fontRenderer, Translation.ADVANCED_CHAT_VISIBILITY,
//                screen.width / 2 - 50, screen.height / 2 - 40, -1))
//        screen.addWidget(SelectWidget(
//                settings.advanced::visibility, EnumValues(LocalVisibility.values()),
//                screen.width / 2 + 10, screen.height / 2 - 40, 40, 13, "Chat Visibility"
//        ))
//
////        add(Label(mc.fontRenderer, Translation.ADVANCED_HIDE_DELIMS,
////                    screen.width), intArrayOf(2, 5))
//        screen.addWidget(FieldBackedCheckboxButton(settings.advanced::hideTag,
//                screen.width / 2 - 40, screen.height / 2, 80, 20,
//                Translation.ADVANCED_HIDE_DELIMS.translate()))
//
////        add(Label(Translation.ADVANCED_SPELLCHECK), intArrayOf(2, 6))
//        screen.addWidget(FieldBackedCheckboxButton(settings.advanced::spelling,
//                screen.width / 2 - 40, screen.height / 2 + 30, 80, 20, Translation.ADVANCED_SPELLCHECK.translate()))
//
//        screen.addRender(Label(mc.fontRenderer, Translation.EXPERIMENTAL,
//                screen.width / 2 - 80, screen.height / 2 + 60))
//    }
//}
