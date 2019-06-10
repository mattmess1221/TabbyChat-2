package mnm.mods.tabbychat.client.gui.component.config;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.Color;
import mnm.mods.tabbychat.util.ILocation;
import mnm.mods.tabbychat.util.config.Value;
import mnm.mods.tabbychat.client.gui.component.GuiSelectColor;
import net.minecraft.util.ResourceLocation;

import java.util.function.Consumer;

/**
 * A setting for colors. When clicked, shows a {@link GuiSelectColor}.
 *
 * @author Matthew
 */
public class GuiSettingColor extends GuiSetting<Color> implements Consumer<Color> {

    private static final ResourceLocation TRANSPARENCY = new ResourceLocation(TabbyChat.MODID, "textures/transparency.png");

    private Color value;

    public GuiSettingColor(Value<Color> setting) {
        super(setting);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (getLocation().contains(x, y) && button == 0) {
            getParent().ifPresent(p -> p.setOverlay(new GuiSelectColor(GuiSettingColor.this, getValue())));
            return true;
        }
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float parTicks) {
        ILocation loc = getLocation();
        mc.getTextureManager().bindTexture(TRANSPARENCY);
        blit(0, 0, 0, 0, loc.getWidth(), loc.getHeight(), 6, 6);
        fill(0, 0, loc.getWidth(), loc.getHeight(), getValue().getHex());
        fill(0, 0, loc.getWidth(), 1, 0xffaabbcc);
        fill(1, 0, 0, loc.getHeight(), 0xffaabbcc);
        fill(loc.getWidth() - 1, 0, loc.getWidth(), loc.getHeight(), 0xffaabbcc);
        fill(0, loc.getHeight(), loc.getWidth(), loc.getHeight() - 1, 0xffaabbcc);
    }

    @Override
    public void accept(Color input) {
        setValue(input);
        getParent().ifPresent(p -> p.setOverlay(null));
    }

    @Override
    public Color getValue() {
        return value;
    }

    @Override
    public void setValue(Color color) {
        this.value = color;
    }

}
