package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.*

class AdvancedSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {

    val chatLocation by child(::LocationConfig)
    var unfocHeight by defining(0.5f)
    var fadeTime by defining(200)
    var historyLen by defining(100)
    var hideTag by defining(false)
    var keepChatOpen by defining(false)
    var spelling by defining(true)
    var visibility by definingEnum(LocalVisibility.NORMAL, LocalVisibility::class.java)

    class LocationConfig(config: AbstractConfigView, path: List<String>) : ConfigView(config, path), ILocation {
        override var xPos by defining(5)
        override var yPos by defining(17)
        override var width by defining(300)
        override var height by defining(160)

        fun merge(loc: ILocation) {
            xPos = loc.xPos
            yPos = loc.yPos
            width = loc.width
            height = loc.height
        }

        override fun asImmutable() = ImmutableLocation(xPos, yPos, width, height)
    }
}
