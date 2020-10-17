package mnm.mods.tabbychat.client.extra.spell
//
//import mnm.mods.tabbychat.TabbyChat
//import mnm.mods.tabbychat.client.TabbyChatClient
//import mnm.mods.tabbychat.client.gui.component.GuiComponent
//import mnm.mods.tabbychat.client.gui.Label
//import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
//import mnm.mods.tabbychat.client.gui.settings.SettingPanel
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.client.settings.TabbySettings
//import mnm.mods.tabbychat.util.*
//import net.minecraft.client.Minecraft
//import net.minecraft.client.gui.widget.button.Button
//import net.minecraft.client.gui.widget.list.AbstractList
//import net.minecraftforge.client.ForgeHooksClient
//import net.minecraftforge.resource.VanillaResourceType
//
//class GuiSettingsSpelling : SettingPanel<TabbySettings> {
//    override val settings: TabbySettings = TabbyChatClient.settings
//
//    val wordLists = (TabbyChatClient.spellcheck as? JazzySpellcheck)?.wordLists
//    val missing get() = wordLists?.getMissingLocales() ?: emptySet()
//
//    init {
//        this.layout = GuiGridLayout(10, 20)
//        this.secondaryColor = Color(255, 215, 0, 64)
//    }
//
//    override fun init(screen: IScreenHelper) {
//        if (wordLists == null) {
//            add(Label(Translation.SPELLCHECK_NOPE.toComponent()))
//        } else {
//            add(Button(Translation.SPELLCHECK_DOWNLOAD_LISTS.translate(), ::download), intArrayOf(0, 0, 4, 2)) {
//                active = missing.isNotEmpty()
//            }
//
//            // TODO translations
//            if (missing.isEmpty()) {
//                add(Label("No missing locales (yet)".toComponent()), intArrayOf(0, 2))
//            } else {
//                add(Label("Click this button to download missing word lists.".toComponent()), intArrayOf(0, 2))
//                for ((y, loc) in missing.withIndex()) {
//                    add(Label(loc.toString().toComponent()), intArrayOf(1, y + 3))
//                }
//            }
//        }
//    }
//
//    private fun download(it: Button) {
//        wordLists!!
//        it.active = false
//        if (missing.isNotEmpty()) {
//            TabbyChat.logger.info("Downloading word lists")
//            wordLists.downloadAll(missing).thenAccept { futures ->
//                for (f in futures) {
//                    try {
//                        val wl = f.join()
//                        TabbyChat.logger.info("downloaded word list for {} to {}", wl.locale, wl.file)
//                    } catch (e: Exception) {
//                        TabbyChat.logger.error("failed to download word list", e)
//                    }
//                }
//                ForgeHooksClient.refreshResources(mc, VanillaResourceType.LANGUAGES)
//            }
//        }
//    }
//
//    private class WordList : GuiComponent() {
//        val list = StringList(mc, 80, 120, 60, 180, 13)
//
//        override var location: ILocation
//            get() = super.location
//            set(value) {
//                list.updateSize(value.xPos, value.yPos, value.width, value.height)
//                list.setLeftPos(value.xPos)
//                super.location = value
//            }
//
//    }
//
//    private class StringList(mcIn: Minecraft, widthIn: Int, heightIn: Int, topIn: Int, bottomIn: Int, itemHeightIn: Int)
//        : AbstractList<StringList.ListEntry>(mcIn, widthIn, heightIn, topIn, bottomIn, itemHeightIn) {
//
//        fun add(item: String) = addEntry(ListEntry(item))
//
//        inner class ListEntry(val item: String) : AbstractList.AbstractListEntry<ListEntry>() {
//            override fun render(row: Int, top: Int, left: Int, width: Int, height: Int, mouseX: Int, mouseY: Int, hover: Boolean, partial: Float) {
//                minecraft.fontRenderer.drawString(item, top.toFloat(), left.toFloat(), -1)
//            }
//
//        }
//    }
//}