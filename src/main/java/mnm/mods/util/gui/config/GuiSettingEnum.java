package mnm.mods.util.gui.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import mnm.mods.util.Color;
import mnm.mods.util.ILocation;
import mnm.mods.util.config.Value;
import mnm.mods.util.gui.events.GuiMouseEvent;
import mnm.mods.util.gui.events.GuiMouseEvent.MouseEvent;
import net.minecraft.client.gui.Gui;
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

    @Subscribe
    public void activate(GuiMouseEvent event) {
        if (event.getType() == MouseEvent.CLICK) {
            int selected = this.values.indexOf(value);
            int max = this.values.size();
            int mov = 0;

            if (event.getButton() == 0) {
                // Left click, go forward
                mov = 1;
            } else if (event.getButton() == 1) {
                // Right click, go backward
                mov = -1;
            }

            if (mov != 0) {
                // find the index and increment or decrement
                int id = selected + mov;
                id = id < 0 ? (max - id) % max : id % max;

                setValue(values.get(id));
            }
        }
    }

    @Override
    public void drawComponent(int mouseX, int mouseY) {
        ILocation loc = this.getLocation();
        Gui.drawRect(0, 0, loc.getWidth(), loc.getHeight(), 0xff000000);
        String string = mc.fontRendererObj.trimStringToWidth(text, loc.getWidth());
        int xPos = loc.getWidth() / 2 - mc.fontRendererObj.getStringWidth(string) / 2;
        int yPos = loc.getHeight() / 2 - 4;
        mc.fontRendererObj.drawString(string, xPos, yPos, getPrimaryColorProperty().getHex());
        drawBorders(0, -1, loc.getWidth(), loc.getHeight() + 1);
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
