package mnm.mods.tabbychat.client.gui.component.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.config.Value;
import net.minecraft.client.resources.I18n;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A setting for set values, such as enums. Values should either have a
 * toString() method or provide a names array during construction.
 *
 * @param <T> The type
 * @author Matthew
 */
public class GuiSettingEnum<T> extends GuiSetting<T> {

    private final List<T> values;
    private final Function<T, String> namer;

    private T value;

    private String text;

    public GuiSettingEnum(Value<T> setting, T[] values) {
        this(setting, Arrays.asList(values), Objects::toString);
    }

    @Deprecated
    public GuiSettingEnum(Value<T> setting, T[] values, String[] names) {
        this(setting, Arrays.asList(values), val -> {
            int i = ArrayUtils.indexOf(values, val);
            return names[i];
        });
        Preconditions.checkArgument(values.length == names.length);
    }

    public GuiSettingEnum(Value<T> setting, List<T> values, Function<T, String> namer) {
        super(setting);
        this.namer = namer;
        this.values = ImmutableList.copyOf(values);

        setValue(setting.get());
        setSecondaryColor(Color.DARK_GRAY);

    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (getLocation().contains(x, y)) {
            int selected = this.values.indexOf(value);
            int max = this.values.size();
            int mov = 0;

            if (button == 0) {
                // Left click, go forward
                mov = 1;
            } else if (button == 1) {
                // Right click, go backward
                mov = -1;
            }

            if (mov != 0) {
                // find the index and increment or decrement
                int id = selected + mov;
                id = id < 0 ? (max - id) % max : id % max;

                setValue(values.get(id));
            }
            return true;
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        ILocation loc = this.getLocation();
        fill(loc.getXPos(), loc.getYPos(), loc.getXWidth(), loc.getYHeight(), 0xff000000);
        String string = mc.fontRenderer.trimStringToWidth(text, loc.getWidth());
        int xPos = loc.getXCenter() - mc.fontRenderer.getStringWidth(string) / 2;
        int yPos = loc.getYCenter() - 4;
        mc.fontRenderer.drawString(string, xPos, yPos, getPrimaryColorProperty().getHex());
        renderBorders(loc.getXPos(), loc.getYPos(), loc.getXWidth(), loc.getYHeight());
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;

        String text = this.value.toString();
        if (namer != null) {
            text = namer.apply(this.value);
        }
        this.text = I18n.format(text);
    }
}
