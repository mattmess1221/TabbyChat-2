package mnm.mods.tabbychat.extra.log

import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.div
import java.nio.file.Path

class ChatLoggingConfig(path: Path) : FileConfigView(path / "logging.toml") {
    @Comment("Logs chat to a file")
    var logChat by defining(true)

    @Comment("When logging, splits the log by channel")
    var splitLog by defining(true)
}