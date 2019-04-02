package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.ILocation;
import net.minecraft.client.gui.Gui;

/**
 * The color variant of {@link GuiSlider}.
 *
 * @author Matthew
 */
public class GuiSliderColor extends GuiSlider {

    private Model model;
    private Color base;

    public GuiSliderColor(double value, boolean vertical, Model model, Color base) {
        super(value, vertical);
        this.model = model;
        this.base = base;
    }

    @Override
    protected void drawMid(ILocation loc) {
        if (isVertical()) {
            for (int i = 0; i < loc.getHeight(); i++) {
                int color = (int) ((double) i / (double) loc.getHeight() * 255D);
                color = Math.abs(color - 256);
                color = getColor(color);
                Gui.drawRect(loc.getXPos(), loc.getYPos() + i, loc.getXWidth(), loc.getYPos() + i + 1, color);
            }
        } else {
            for (int i = 0; i < loc.getWidth(); i++) {
                int color = (int) ((double) i / (double) loc.getWidth() * 255D);
                // color = Math.abs(color - 256);
                color = getColor(color);
                Gui.drawRect(loc.getXPos() + i, loc.getYPos(), loc.getXPos() + i + 1, loc.getYHeight(), color);
            }
        }
    }

    @Override
    public String getFormattedValue() {
        return Integer.toString((int) (getValue() * 255D));
    }

    private int getColor(int i) {
        i %= 256;
        switch (model) {
            case RED:
                return Color.getColor(i, base.getGreen(), base.getBlue(), base.getAlpha());
            case GREEN:
                return Color.getColor(base.getRed(), i, base.getBlue(), base.getAlpha());
            case BLUE:
                return Color.getColor(base.getRed(), base.getGreen(), i, base.getAlpha());
            case ALPHA:
                return Color.getColor(base.getRed(), base.getGreen(), base.getBlue(), i);
        }
        return -1;
    }

    public void setBase(Color color) {
        this.base = color;
    }

    /**
     * Base colors from the rgba color model
     */
    public enum Model {
        RED,
        GREEN,
        BLUE,
        ALPHA
    }
}
