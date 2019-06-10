package mnm.mods.tabbychat.client.gui.component;

import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.Location;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.text.NumberFormat;
import javax.annotation.Nonnull;

/**
 * Input for numbers, also known as a slider. Use {@link DoubleUpDown} for
 * doubles and {@link IntUpDown} for integers.
 *
 * @param <T> The number type.
 * @author Matthew
 */
public abstract class GuiNumericUpDown<T extends Number> extends GuiPanel implements IGuiInput<T> {

    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;
    private double interval = 1;

    private double value;

    private NumberFormat format = NumberFormat.getNumberInstance();

    private GuiNumericUpDown() {
        setLayout(new BorderLayout());

        {
            GuiPanel text = new GuiPanel(new AbsoluteLayout());
            GuiRectangle rect = new GuiRectangle() {
                @Nonnull
                @Override
                public ILocation getLocation() {
                    return getParent().map(GuiComponent::getLocation).orElseGet(super::getLocation);
                }
            };
            rect.setPrimaryColor(Color.BLACK);
            text.addComponent(rect);
            GuiLabel label = new GuiLabel() {
                @Override
                public ITextComponent getText() {
                    return new StringTextComponent(format.format(getValue()));
                }
            };
            text.addComponent(label, new Location(5, 1, 0, 0));

            addComponent(text, BorderLayout.Position.CENTER);
        }
        {
            GuiPanel pnlButtons = new GuiPanel(new AbsoluteLayout());
            GuiButton up = new UpDown("\u2191", 1); // up arrow
            GuiButton down = new UpDown("\u2193", -1); // down arrow
            pnlButtons.addComponent(up, new Location(0, 0, 5, 5));
            pnlButtons.addComponent(down, new Location(0, 5, 5, 5));

            addComponent(pnlButtons, BorderLayout.Position.EAST);
        }
    }

    /**
     * Increments the value by {@code n}. Use a negative number to go down.
     * <p>
     * Essentially runs <code>value = value + (n * interval)</code>
     *
     * @param n The amount to increment
     */
    public void increment(int n) {
        setDouble(getDouble() + n * getInterval());
    }

    /**
     * Sets the number format used drawing the value.
     *
     * @param numberFormat The format
     */
    public void setFormat(NumberFormat numberFormat) {
        this.format = numberFormat;
    }

    /**
     * Gets the minimum value.
     *
     * @return The min
     */
    public double getMin() {
        return min;
    }

    /**
     * Sets the minimum value.
     *
     * @param min The min
     */
    public void setMin(double min) {
        this.min = min;
    }

    /**
     * Gets the maximum value.
     *
     * @return The max
     */
    public double getMax() {
        return max;
    }

    /**
     * Sets the maximum value.
     *
     * @param max The max
     */
    public void setMax(double max) {
        this.max = max;
    }

    /**
     * Gets the interval, which is the amount the value is changed on each
     * increment.
     *
     * @return The interval
     */
    public double getInterval() {
        return interval;
    }

    /**
     * Sets the interval, which is the amount the value is changed on each
     * increment.
     *
     * @param interval The interval
     */

    public void setInterval(double interval) {
        this.interval = interval;
    }

    /**
     * Gets the value as a double.
     *
     * @return The double value
     */
    public double getDouble() {
        return value;
    }

    /**
     * Sets the value as a double.
     *
     * @param value The new value
     */
    public void setDouble(double value) {
        value = Math.max(value, getMin());
        value = Math.min(value, getMax());
        this.value = value;
    }

    private class UpDown extends GuiButton {

        private int direction;

        public UpDown(String text, int direction) {
            super(text);
            this.direction = direction;
            setSecondaryColor(Color.DARK_GRAY);
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            increment(direction);
        }
    }

    /**
     * A Numeric Up Down for integers.
     *
     * @author Matthew
     */
    public static class IntUpDown extends GuiNumericUpDown<Integer> {
        @Override
        public Integer getValue() {
            return (int) getDouble();
        }

        @Override
        public void setValue(Integer i) {
            setDouble(i);
        }
    }

    /**
     * A Numeric Up Down for doubles.
     *
     * @author Matthew
     */
    public static class DoubleUpDown extends GuiNumericUpDown<Double> {
        @Override
        public Double getValue() {
            return getDouble();
        }

        @Override
        public void setValue(Double d) {
            setDouble(d);
        }
    }
}
