package mnm.mods.tabbychat.client.gui.settings

import mnm.mods.tabbychat.TabbyChat
import mnm.mods.tabbychat.client.TabbyChatClient
import mnm.mods.tabbychat.client.extra.spell.JazzySpellcheck
import mnm.mods.tabbychat.client.gui.component.GuiButton
import mnm.mods.tabbychat.client.gui.component.GuiLabel
import mnm.mods.tabbychat.client.gui.component.layout.GuiGridLayout
import mnm.mods.tabbychat.client.settings.TabbySettings
import mnm.mods.tabbychat.util.Color
import mnm.mods.tabbychat.util.Translation

class GuiSettingsSpelling : SettingPanel<TabbySettings>() {
    override val displayString: String by Translation.SETTINGS_SPELLING
    override val settings: TabbySettings = TabbyChatClient.settings

    init {
        this.layout = GuiGridLayout(10, 20)
        this.secondaryColor = Color(255, 215, 0, 64)
    }

    override fun initGUI() {
        val wordLists = (TabbyChatClient.spellcheck as? JazzySpellcheck)?.wordLists

        if (wordLists == null) {
            add(GuiLabel(Translation.SPELLCHECK_NOPE.toComponent()))
        } else {
            add(GuiButton(Translation.SPELLCHECK_DOWNLOAD_LISTS.translate()) {
                checkNotNull(wordLists)
                it.active = false
                val missing = wordLists.getMissingLocales()
                if (missing.isNotEmpty()) {
                    TabbyChat.logger.info("Downloading word lists")
                    wordLists.downloadAll(missing).thenAccept { futures ->
                        for (f in futures) {
                            try {
                                val wl = f.join()
                                TabbyChat.logger.info("downloaded word list for {} to {}", wl.locale, wl.file)
                            } catch (e: Exception) {
                                TabbyChat.logger.error("failed to download word list", e)
                            }
                        }
                    }
                }
            }, intArrayOf(0, 0, 4, 2)) {
                active = (wordLists?.getMissingLocales() ?: emptySet()).isNotEmpty()
            }
        }
    }
}