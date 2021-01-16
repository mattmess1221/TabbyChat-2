package mnm.mods.tabbychat.extra.spam

import mnm.mods.tabbychat.util.config.Comment
import mnm.mods.tabbychat.util.config.FileConfigView
import mnm.mods.tabbychat.util.div
import java.nio.file.Path

class ChatAntiSpamConfig(path: Path) : FileConfigView(path / "spam.toml") {
    @Comment("Merge duplicate chat messages with the previous message")
    var antiSpam by defining(false)

    @Comment("The percentage (0-1) of similarity between two message to count as spam")
    var antiSpamPrejudice by defining(0.0)

}