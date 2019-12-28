package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.ImmutableLocation
import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.config.ConfigView
import mnm.mods.tabbychat.util.config.FileConfigView

class AdvancedSettings(config: FileConfigView, path: List<String>) : ConfigView(config, path) {

    val chatLocation by child(::LocationConfig)
    val unfocHeight by defining(0.5f)
    val fadeTime by defining(200)
    val historyLen by defining(100)
    val hideTag by defining(false)
    val keepChatOpen by defining(false)
    val spelling by defining(true)
    val visibility by definingEnum(LocalVisibility.NORMAL)

    class LocationConfig(config: FileConfigView, path: List<String>) : ConfigView(config, path), ILocation {
        val x by defining(5)
        val y by defining(17)
        val w by defining(300)
        val h by defining(160)

        override var xPos by x
        override var yPos by y
        override var width by w
        override var height by h

        fun merge(loc: ILocation) {
            xPos = loc.xPos
            yPos = loc.yPos
            width = loc.width
            height = loc.height
        }

        override fun asImmutable() = ImmutableLocation(xPos, yPos, width, height)
    }
}
