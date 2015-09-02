package mnm.mods.tabbychat.api.gui;

import mnm.mods.util.gui.GuiComponent;

interface IGui<Gui extends GuiComponent> {

    /**
     * Gets this gui as its underlying gui type. Convenience method for casting
     * to Gui. Implementation should return this.
     *
     * <pre>
     * &#64;Override
     * public GuiComponent asGui() {
     *     return this;
     * }
     * </pre>
     *
     * @return
     */
    Gui asGui();
}