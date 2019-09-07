package mnm.mods.tabbychat.client.gui.component.config

import mnm.mods.tabbychat.util.config.SettingsFile
import mnm.mods.tabbychat.client.gui.component.GuiPanel

/**
 * Base class for a setting panel.
 */
abstract class SettingPanel<T : SettingsFile> : GuiPanel() {

    abstract val displayString: String

    /**
     * Gets the [SettingsFile] used for this category. Used for loading
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
