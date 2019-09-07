package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.client.gui.component.layout.ILayout
import mnm.mods.tabbychat.util.mc

class ChatPanel : GuiPanel {

    override var isVisible: Boolean
        get() = super.isVisible && mc.ingameGUI.chatGUI.chatOpen
        set(value: Boolean) {
            super.isVisible = value
        }

    constructor() : super()

    constructor(layout: ILayout) : super(layout)
}
