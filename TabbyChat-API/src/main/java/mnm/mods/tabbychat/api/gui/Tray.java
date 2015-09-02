package mnm.mods.tabbychat.api.gui;

import mnm.mods.util.gui.GuiPanel;

/**
 * The tray is the upper most section of the chat box. It contains the channel
 * tabs.
 */
public interface Tray extends IGui<GuiPanel> {

    /**
     * Gets a panel containing current tabs.
     *
     * @return A panel of tabs
     */
    GuiPanel getTabList();
}
