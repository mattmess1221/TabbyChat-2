package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.config.ValueObject

class AdvancedSettings : ValueObject<AdvancedSettings>() {

    private val chatX by value { 5 }
    private val chatY by value { 17 }
    private val chatW by value { 300 }
    private val chatH by value { 160 }
    val unfocHeight by value { 0.5f }
    val fadeTime by value { 200 }
    val historyLen by value { 100 }
    val hideTag by value { false }
    val keepChatOpen by value { false }
    val spelling by value { true }
    val visibility by value { LocalVisibility.NORMAL }

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
