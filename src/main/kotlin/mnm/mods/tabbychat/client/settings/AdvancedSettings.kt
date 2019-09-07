package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.ValueObject

class AdvancedSettings : ValueObject() {

    private val chatX = Value(5)
    private val chatY = Value(17)
    private val chatW = Value(300)
    private val chatH = Value(160)
    val unfocHeight = Value(0.5f)
    val fadeTime = Value(200)
    val historyLen = Value(100)
    val hideTag = Value(false)
    val keepChatOpen = Value(false)
    val spelling = Value(true)
    val visibility = Value(LocalVisibility.NORMAL)

    var chatboxLocation: ILocation
        get() = Location(
                chatX.value, chatY.value,
                chatW.value, chatH.value
        )
        set(value) {
            chatX.value = value.xPos
            chatY.value = value.yPos
            chatW.value = value.width
            chatH.value = value.height
        }
}
