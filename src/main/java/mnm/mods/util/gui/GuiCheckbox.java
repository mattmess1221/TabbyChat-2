package mnm.mods.util.gui;

import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.Location;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;

/**
 * A checkbox, representing a boolean input.
 *
 * @author Matthew
 */
public class GuiCheckbox extends GuiButton implements IGuiInput<Boolean> {

    private boolean value;

    public GuiCheckbox() {
        super("");
        this.setLocation(new Location(0, 0, 9, 9));
        setSecondaryColor(Color.of(0x99ffffa0));
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        Minecraft mc = Minecraft.getInstance();

        ILocation loc = getLocation();
        GuiUtils.drawContinuousTexturedBox(WIDGETS, loc.getXPos(), loc.getYPos(), 0, 46, loc.getWidth(), loc.getHeight(), 200, 20, 2, 3, 2, 2, this.zLevel);

        if (this.getValue()) {
            this.drawCenteredString(mc.fontRenderer, "x", loc.getXCenter() + 1, loc.getYPos() + 1, getSecondaryColorProperty().getHex());
        }
        this.drawString(mc.fontRenderer, getText(), loc.getXWidth() + 2, loc.getYPos() + 2, getPrimaryColorProperty().getHex());
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    @Override
    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        setValue(!getValue());
    }

}
