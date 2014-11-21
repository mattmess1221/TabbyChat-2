package mnm.mods.tabbychat.gui;

import mnm.mods.util.gui.GuiButton;
import net.minecraft.client.gui.Gui;

public class PrefsButton extends GuiButton {

    public PrefsButton() {
        this("");
    }

    public PrefsButton(String text) {
        this(0, 0, 100, 20, text);
    }

    public PrefsButton(int x, int y, String text) {
        this(x, y, 100, 20, text);
    }

    public PrefsButton(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        setText(text);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        Gui.drawRect(0, 0, getBounds().width, getBounds().height, getBackColor());
        this.drawCenteredString(mc.fontRendererObj, this.text, this.getBounds().width / 2,
                (this.getBounds().height - 8) / 2, getForeColor());
    }
}
