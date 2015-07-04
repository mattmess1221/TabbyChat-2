package mnm.mods.tabbychat.gui;

import java.awt.Dimension;

import mnm.mods.util.gui.GuiComponent;
import net.minecraft.client.gui.Gui;

public class ChatHandle extends GuiComponent {

    public ChatHandle() {
        setSize(10, 10);
        // setForeColor(-1);
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        Gui.drawRect(4, 4, 10, 5, getForeColor());
        Gui.drawRect(11, 4, 10, 11, getForeColor());
        super.drawComponent(mouseX, mouseY);
    }

    @Override
    public int getForeColor() {
        return isHovered() ? 0xaaffffa0 : super.getForeColor();
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(15, 15);
    }
}
