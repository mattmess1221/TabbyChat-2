package mnm.mods.tabbychat.util.config

object ConfigManager {
    private val configs = ArrayList<() -> Iterable<FileConfigView>>()

    private val allConfigs: Sequence<FileConfigView> get() = configs.asSequence().flatMap { it().asSequence() }

    fun addConfigs(vararg configs: FileConfigView) {
        this.configs += { configs.asIterable() }
    }

    fun addConfigs(configs: () -> Iterable<FileConfigView>) {
        this.configs += configs
    }

    fun save() {
        allConfigs.forEach(FileConfigView::save)
    }

    fun load() {
        allConfigs.forEach(FileConfigView::load)
    }
}