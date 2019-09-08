package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location
import mnm.mods.tabbychat.util.config.AbstractValue
import mnm.mods.tabbychat.util.config.Value
import mnm.mods.tabbychat.util.config.ValueObject

class AdvancedSettings : ValueObject<AdvancedSettings>() {

    private val chatX: Value<Int> by value { 5 }
    private val chatY: Value<Int> by value { 17 }
    private val chatW: Value<Int> by value { 300 }
    private val chatH: Value<Int> by value { 160 }
    val unfocHeight: Value<Float> by value { 0.5f }
    val fadeTime: Value<Int> by value { 200 }
    val historyLen: Value<Int> by value { 100 }
    val hideTag: Value<Boolean> by value { false }
    val keepChatOpen: Value<Boolean> by value { false }
    val spelling: Value<Boolean> by value { true }
    val visibility: Value<LocalVisibility> by value { LocalVisibility.NORMAL }

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
