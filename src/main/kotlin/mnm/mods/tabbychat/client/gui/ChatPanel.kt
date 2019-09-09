package mnm.mods.tabbychat.client.gui

import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.mc

class ChatPanel : GuiPanel() {

    override var visible: Boolean
        get() = super.visible && mc.ingameGUI.chatGUI.chatOpen
        set(value) {
            super.visible = value
        }

}
