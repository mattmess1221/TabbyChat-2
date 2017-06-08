package mnm.mods.util.gui;

import com.google.common.eventbus.Subscribe;

import mnm.mods.util.Color;
import mnm.mods.util.Location;
import mnm.mods.util.gui.events.ActionPerformedEvent;
import net.minecraft.client.gui.Gui;

/**
 * A checkbox, representing a boolean input.
 *
 * @author Matthew
 */
public class GuiCheckbox extends GuiComponent implements IGuiInput<Boolean> {

    private boolean value;

    public GuiCheckbox() {
        this.setLocation(new Location(0, 0, 9, 9));
        setSecondaryColor(Color.of(0x99ffffa0));
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        int centerX = 4;
        int centerY = 4;
        int width = 9;
        int height = 9;

        int fore = getPrimaryColorProperty().getHex();
        int back = getSecondaryColorProperty().getHex();

        // Background
        Gui.drawRect(1, 0, width - 1, 1, back); // top
        Gui.drawRect(1, height - 1, width - 1, height, back); // bottom
        Gui.drawRect(0, 1, 1, height - 1, back); // left
        Gui.drawRect(width - 1, 1, width, height - 1, back); // right
        Gui.drawRect(1, 1, width - 1, height - 1, 0xff000000); // background

        if (getValue()) {
            // draw check
            Gui.drawRect(centerX - 2, centerY, centerX - 1, centerY + 1, fore);
            Gui.drawRect(centerX - 1, centerY + 1, centerX, centerY + 2, fore);
            Gui.drawRect(centerX, centerY + 2, centerX + 1, centerY + 3, fore);
            Gui.drawRect(centerX + 1, centerY + 2, centerX + 2, centerY, fore);
            Gui.drawRect(centerX + 2, centerY, centerX + 3, centerY - 2, fore);
            Gui.drawRect(centerX + 3, centerY - 2, centerX + 4, centerY - 4, fore);
        }
        super.drawComponent(mouseX, mouseY);
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Subscribe
    public void onClick(ActionPerformedEvent event) {
        setValue(!getValue());
    }
}
