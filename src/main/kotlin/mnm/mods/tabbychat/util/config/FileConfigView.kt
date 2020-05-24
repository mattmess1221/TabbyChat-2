package mnm.mods.tabbychat.util.config

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import java.nio.file.Files
import java.nio.file.Path

abstract class FileConfigView(path: Path) : AbstractConfigView() {

    override val config: CommentedFileConfig = CommentedFileConfig.of(path)
    override val path: List<String> = emptyList()

    fun save() {
        Files.createDirectories(config.nioPath.parent)
        config.save()
    }

    fun load() {
        if (Files.exists(config.nioPath)) {
            config.load()
        }
        this.populateDefaults()
        save()
    }
}