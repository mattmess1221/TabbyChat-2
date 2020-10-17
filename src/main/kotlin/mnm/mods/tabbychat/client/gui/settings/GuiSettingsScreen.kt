package mnm.mods.tabbychat.client.gui.settings
//
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.util.Translatable
//import mnm.mods.tabbychat.util.Translation
//import mnm.mods.tabbychat.util.translate
//import net.minecraft.client.gui.IRenderable
//import net.minecraft.client.gui.screen.Screen
//import net.minecraft.client.gui.widget.Widget
//import net.minecraft.client.gui.widget.button.Button
//
//class GuiSettingsScreen(private val selectedSetting: SettingPanel<*>?)
//    : Screen(Translation.SETTINGS_TITLE.toComponent()) {
//
//    val renderers = ArrayList<IRenderable>()
//
//    override fun init() {
//        if (selectedSetting != null) {
//            selectedSetting.init(ScreenHelper())
//        } else {
//            settings.forEachIndexed { index, (title, getter) ->
//                addButton(Button(width / 2 - 20, index * 30 + 80, 40, 20, title.translate()) {
//                    selectSetting(getter())
//                })
//            }
//        }
//
//        addButton(Button(20, height - 20, 80, 20, "Close") {
//            selectSetting(null)
//        })
//    }
//
//    override fun render(mouseX: Int, mouseY: Int, partialTicks: Float) {
//        this.renderBackground()
//        super.render(mouseX, mouseY, partialTicks)
//        this.renderers.forEach {
//            it.render(mouseX, mouseY, partialTicks)
//        }
//    }
//
//    override fun removed() {
//        selectedSetting?.settings?.save()
//    }
//
//    private fun selectSetting(setting: SettingPanel<*>?) {
//        if (this.selectedSetting == null) {
//            minecraft!!.displayGuiScreen(null)
//        } else {
//            minecraft!!.displayGuiScreen(GuiSettingsScreen(setting))
//        }
//    }
//
//    companion object {
//        private val settings: List<(Pair<Translatable, () -> SettingPanel<*>>)> = listOf(
//                Translation.SETTINGS_GENERAL to { GuiSettingsGeneral() },
//                Translation.SETTINGS_SERVER to { GuiSettingsServer() },
//                Translation.CHANNEL_TITLE to { GuiSettingsChannel() },
//                Translation.SETTINGS_FILTERS to { GuiSettingsFilters() },
//                Translation.SETTINGS_ADVANCED to { GuiAdvancedSettings() }
//        )
//    }
//
//    private inner class ScreenHelper : IScreenHelper {
//        override val width get() = this@GuiSettingsScreen.width
//        override val height get() = this@GuiSettingsScreen.height
//
//        override fun <T : Widget> addWidget(widget: T, init: T.() -> Unit): T {
//            init(widget)
//            this@GuiSettingsScreen.addButton(widget)
//            return widget
//        }
//
//        override fun <T : IRenderable> addRender(render: T, init: T.() -> Unit) = render.also {
//            init(it)
//            this@GuiSettingsScreen.renderers.add(it)
//        }
//    }
//}
