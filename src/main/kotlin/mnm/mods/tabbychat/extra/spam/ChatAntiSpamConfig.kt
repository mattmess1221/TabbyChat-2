package mnm.mods.tabbychat.extra.spam

import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView

class ChatAntiSpamConfig(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {
    @Comment("Merge duplicate chat messages with the previous message")
    var antiSpam by defining(false)

    @Comment("The percentage (0-1) of similarity between two message to count as spam")
    var antiSpamPrejudice by defining(0.0)

}