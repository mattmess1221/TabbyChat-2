package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.client.gui.component.GuiPanel
import mnm.mods.tabbychat.util.config.FileConfigView

/**
 * Base class for a setting panel.
 */
abstract class SettingPanel<T : FileConfigView> : GuiPanel() {

    abstract val displayString: String

    /**
     * Gets the [FileConfigView] used for this category. Used for loading
     * and saving the settings file.
     *
     * @return The settings
     */
    abstract val settings: T

    /**
     * Called when this category is activated and displayed.
     */
    open fun initGUI() {}

}
