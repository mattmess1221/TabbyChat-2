package mnm.mods.tabbychat.util.config

object ConfigManager {
    private val configs = ArrayList<() -> FileConfigView>()

    private val allConfigs get() = configs.asSequence().map { it() }

    fun register(configs: () -> FileConfigView) {
        this.configs += configs
    }

    fun save() {
        allConfigs.forEach(FileConfigView::save)
    }

    fun load() {
        allConfigs.forEach(FileConfigView::load)
    }
}