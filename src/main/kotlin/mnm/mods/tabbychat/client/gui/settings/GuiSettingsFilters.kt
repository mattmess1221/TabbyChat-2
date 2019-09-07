package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.extra.filters.GuiFilterEditor
import mnm.mods.tabbychat.client.extra.filters.UserFilter
import mnm.mods.tabbychat.client.gui.component.GuiButton
import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.client.gui.component.config.SettingPanel
import mnm.mods.tabbychat.client.gui.component.layout.BorderLayout
import mnm.mods.tabbychat.client.gui.component.layout.FlowLayout
import mnm.mods.tabbychat.client.settings.ServerSettings
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Translation.FILTERS

class GuiSettingsFilters internal constructor() : SettingPanel<ServerSettings>() {

    override val displayString by FILTERS
    override val settings: ServerSettings = TabbyChatClient.serverSettings!!

    private var currentFilter: GuiFilterEditor? = null

    private var index = 0

    private lateinit var prev: GuiButton
    private lateinit var next: GuiButton
    private lateinit var delete: GuiButton


    init {
        layout = BorderLayout()
        secondaryColor = Color(50, 200, 50, 64)
    }

    override fun initGUI() {
        index = settings.filters.size - 1

        val panel = add(GuiPanel(FlowLayout()), BorderLayout.Position.NORTH)

        prev = panel.add(object : GuiButton("<") {
            override fun onClick(mouseX: Double, mouseY: Double) {
                select(index - 1)
            }
        })

        panel.add(object : GuiButton("+") {
            override fun onClick(mouseX: Double, mouseY: Double) {
                add()
            }
        })
        delete = panel.add( object : GuiButton("-") {
            override fun onClick(mouseX: Double, mouseY: Double) {
                delete(index)
            }
        })
        next = panel.add(object : GuiButton(">") {
            override fun onClick(mouseX: Double, mouseY: Double) {
                select(index + 1)
            }
        })

        prev.isEnabled = false
        if (index == -1) {
            delete.isEnabled = false
            next.isEnabled = false
        } else {
            select(index)
        }

        update()
    }

    private fun select(i: Int) {
        this.index = i
        currentFilter?.let {remove(it)}

        val filter = settings.filters[i]
        currentFilter = this.add(GuiFilterEditor(filter),  BorderLayout.Position.CENTER)
        focused = currentFilter

        update()
    }

    private fun delete(i: Int) {
        // deletes a filter
        settings.filters.removeAt(i)
        this.remove(this.currentFilter!!)
        update()
    }

    private fun add() {
        // creates a new filter, adds it to the list, and selects it.
        settings.filters.add(UserFilter())
        select(settings.filters.size - 1)
        update()
    }

    private fun update() {
        this.next.isEnabled = true
        this.prev.isEnabled = true
        this.delete.isEnabled = true

        val size = settings.filters.size

        if (index >= size - 1) {
            this.next.isEnabled = false
            index = size - 1
        }
        if (index < 1) {
            this.prev.isEnabled = false
            index = 0
        }
        if (size < 1) {
            this.delete.isEnabled = false
            this.index = 0
        }
    }
}
