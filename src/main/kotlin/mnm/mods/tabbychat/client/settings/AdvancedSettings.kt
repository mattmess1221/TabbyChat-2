package mnm.mods.tabbychat.client.settings

import com.electronwill.nightconfig.core.Config
import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.ImmutableLocation
import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.ConfigView

class AdvancedSettings(config: Config) : ConfigView(config) {

    val chatLocation by child(::LocationConfig)
    var unfocHeight by defining(0.5f)
    var fadeTime by defining(200)
    var historyLen by defining(100)
    var hideTag by defining(false)
    var keepChatOpen by defining(false)
    var spelling by defining(true)
    var visibility by definingEnum(LocalVisibility.NORMAL)

    class LocationConfig(config: Config) : ConfigView(config), ILocation {
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
