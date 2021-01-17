package mnm.mods.tabbychat.extra.log

import mnm.mods.tabbychat.util.config.AbstractConfigView
import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.ConfigView

class ChatLoggingConfig(config: AbstractConfigView, path: List<String>) : ConfigView(config, path) {
    @Comment("Logs chat to a file")
    var logChat by defining(true)

    @Comment("When logging, splits the log by channel")
    var splitLog by defining(true)
}