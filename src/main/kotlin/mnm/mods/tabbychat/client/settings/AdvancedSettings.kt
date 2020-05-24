package mnm.mods.tabbychat.client.settings

import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.ImmutableLocation
import mnm.mods.tabbychat.util.LocalVisibility
import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView

class AdvancedSettings(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {

    @Comment("The location of the chatbox.\n" +
            "To move, drag chatbox around using the top panel.\n" +
            "To resize, drag the triangle in the top right corner of the chatbox.")
    val chatLocation by child(::LocationConfig)
    @Comment("The height percentage to show chat messages when chat is not open")
    var unfocHeight by defining(0.5f)
    @Comment("How long it takes for a message to fade away when chat is not open")
    var fadeTime by defining(200)
    @Comment("How many chat message to keep in the buffer at a time.\n" +
            "Warning: Setting this too high may cause performance issues.")
    var historyLen by defining(100)
    @Comment("Attempts to remove the matched tag from the message when added to a channel.")
    var hideTag by defining(false)
    @Comment("Keeps the chat open after sending a message.\n" +
            "Can be toggled using the square in the top right corner of the chatbox")
    var keepChatOpen by defining(false)
    @Comment("Enables spellcheck for chat input")
    var spelling by defining(true)
    @Comment("An additional visibility setting\n" +
            "ALWAYS: always renders the chatbox as if it were open\n" +
            "NORMAL: uses the settings from vanilla\n" +
            "HIDDEN: hides chat when the chatbox is not open")
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
