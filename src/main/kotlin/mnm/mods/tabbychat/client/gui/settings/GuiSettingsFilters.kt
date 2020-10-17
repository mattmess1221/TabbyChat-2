package mnm.mods.tabbychat.client.gui.settings
//
//import mnm.mods.tabbychat.client.TabbyChatClient
//import mnm.mods.tabbychat.client.extra.filters.GuiFilterEditor
//import mnm.mods.tabbychat.client.extra.filters.UserFilter
//import mnm.mods.tabbychat.client.gui.IScreenHelper
//import mnm.mods.tabbychat.client.settings.ServerSettings
//import mnm.mods.tabbychat.util.Color
//import net.minecraft.client.gui.widget.button.Button
//
//class GuiSettingsFilters : SettingPanel<ServerSettings> {
//
//    override val settings: ServerSettings = TabbyChatClient.serverSettings
//
//    private val filters = settings.filters
//    private var currentFilter: GuiFilterEditor? = null
//
//    private var index = 0
//
//    private lateinit var prev: Button
//    private lateinit var next: Button
//    private lateinit var delete: Button
//
//    val secondaryColor = Color(50, 200, 50, 64)
//
//    override fun init(screen: IScreenHelper) {
//        index = filters.size - 1
//
//        val panel = add(GuiPanel(), BorderLayout.Position.NORTH) {
//            layout = FlowLayout()
//        }
//
//        prev = panel.add(GuiButton("<") {
//            select(index - 1)
//        })
//
//        panel.add(GuiButton("+") {
//            add()
//        })
//        delete = panel.add(GuiButton("-") {
//            delete(index)
//        })
//        next = panel.add(GuiButton(">") {
//            select(index + 1)
//        })
//
//        prev.active = false
//        if (index == -1) {
//            delete.active = false
//            next.active = false
//        } else {
//            currentFilter?.parent = this
//            select(index)
//        }
//
//        update()
//    }
//
//    private fun select(i: Int) {
//        this.index = i
//        currentFilter?.let { remove(it) }
//
//        val filter = filters[i]
//        currentFilter = this.add(GuiFilterEditor(filter), BorderLayout.Position.CENTER)
//        setFocused(currentFilter)
//
//        update()
//    }
//
//    private fun delete(i: Int) {
//        // deletes a filter
//        filters.removeAt(i)
//        this.remove(this.currentFilter!!)
//        this.currentFilter = null
//        update()
//    }
//
//    private fun add() {
//        // creates a new filter, adds it to the list, and selects it.
//        filters.add(UserFilter())
//        select(filters.size - 1)
//        update()
//    }
//
//    private fun update() {
//        this.next.active = true
//        this.prev.active = true
//        this.delete.active = true
//
//        val size = filters.size
//
//        if (index >= size - 1) {
//            this.next.active = false
//            index = size - 1
//        }
//        if (index < 1) {
//            this.prev.active = false
//            index = 0
//        }
//        if (size < 1) {
//            this.delete.active = false
//            this.index = 0
//        }
//
//    }
//}
